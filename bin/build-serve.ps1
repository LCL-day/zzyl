# Compile zzyl-serve and sync artifacts into target/classes
# Purpose: let the running app (IDEA + devtools) pick up the latest classes and mappers.
#
# Pitfalls this script avoids:
#  1) robocopy returns 1..7 and silently skips files that the running JVM has locked,
#     which used to leave a missing .class behind (symptom: endpoints suddenly 404).
#     -> use Copy-Item -Force plus per-file verification.
#  2) Get-ChildItem -Recurse -Filter can miss files here; always use -Recurse -File + Where-Object.
#  3) Keep this file ASCII-only: PowerShell 5.1 reads .ps1 as GBK and would corrupt CJK literals.
$ErrorActionPreference = 'Stop'
$root    = 'C:\Users\Nine\Desktop\zzyl'
$repo    = Join-Path $root 'respository_zzyl'
$javaBin = Join-Path $root 'jdk-11.0.20\jdk-11.0.20\bin'
$serve   = Join-Path $root 'zzyl\zzyl-serve'
$dst     = Join-Path $serve 'target\classes'
$out     = Join-Path $env:TEMP 'zzyl-serve-build'

# ---------- 1. build classpath ----------
$cp = @(
    (Join-Path $root 'zzyl\zzyl-common\target\classes'),
    (Join-Path $root 'zzyl\zzyl-system\target\classes'),
    (Join-Path $root 'zzyl\zzyl-framework\target\classes')
)
$jars = @(
    'spring-web-5.3.33.jar', 'spring-context-5.3.33.jar', 'spring-beans-5.3.33.jar', 'spring-core-5.3.33.jar',
    'spring-security-core-5.7.12.jar', 'spring-boot-autoconfigure-2.5.15.jar', 'mybatis-3.5.13.jar',
    'commons-lang3-3.12.0.jar', 'slf4j-api-1.7.36.jar', 'pagehelper-5.3.3.jar',
    'jakarta.servlet-api-4.0.4.jar', 'javax.servlet-api-4.0.1.jar',
    'jackson-annotations-2.12.7.jar', 'jackson-annotations-2.13.5.jar', 'jackson-core-2.12.7.jar'
)
foreach ($jar in $jars) {
    $found = Get-ChildItem $repo -Recurse -File | Where-Object { $_.Name -eq $jar } | Select-Object -First 1
    if ($found) { $cp += $found.FullName }
}
$cp = $cp | Where-Object { $_ -and (Test-Path $_) } | Select-Object -Unique

# ---------- 2. compile ----------
if (Test-Path $out) { Remove-Item -Recurse -Force $out -ErrorAction SilentlyContinue }
New-Item -ItemType Directory -Force -Path $out | Out-Null

$srcs = @(Get-ChildItem (Join-Path $serve 'src\main\java') -Recurse -File | Where-Object { $_.Extension -eq '.java' })
& (Join-Path $javaBin 'javac.exe') -encoding UTF-8 -nowarn -d $out -cp ($cp -join ';') ($srcs | Select-Object -ExpandProperty FullName)
if ($LASTEXITCODE -ne 0) { throw "javac failed: $LASTEXITCODE" }

# ---------- 3. sync classes and resources ----------
if (-not (Test-Path $dst)) { New-Item -ItemType Directory -Force -Path $dst | Out-Null }
$resSrc = Join-Path $serve 'src\main\resources'

$copied = 0
$failedFiles = @()
foreach ($f in (Get-ChildItem $out -Recurse -File)) {
    $rel = $f.FullName.Substring($out.Length).TrimStart('\')
    $target = Join-Path $dst $rel
    $dir = Split-Path $target -Parent
    if (-not (Test-Path $dir)) { New-Item -ItemType Directory -Force -Path $dir | Out-Null }
    try {
        Copy-Item -LiteralPath $f.FullName -Destination $target -Force -ErrorAction Stop
        $copied++
    } catch {
        $failedFiles += $rel
    }
}
foreach ($f in (Get-ChildItem $resSrc -Recurse -File)) {
    $rel = $f.FullName.Substring($resSrc.Length).TrimStart('\')
    $target = Join-Path $dst $rel
    $dir = Split-Path $target -Parent
    if (-not (Test-Path $dir)) { New-Item -ItemType Directory -Force -Path $dir | Out-Null }
    try {
        Copy-Item -LiteralPath $f.FullName -Destination $target -Force -ErrorAction Stop
        $copied++
    } catch {
        $failedFiles += $rel
    }
}

# ---------- 4. verify every artifact ----------
$mismatch = @()
foreach ($f in (Get-ChildItem $out -Recurse -File)) {
    $rel = $f.FullName.Substring($out.Length).TrimStart('\')
    $target = Join-Path $dst $rel
    if (-not (Test-Path $target)) { $mismatch += "$rel (missing)" }
    elseif ((Get-Item $target).Length -ne $f.Length) { $mismatch += "$rel (size mismatch)" }
}
foreach ($name in @('AppFoodController.class', 'AppAuthController.class')) {
    $p = Join-Path $dst ('com\zzyl\serve\controller\' + $name)
    if (-not (Test-Path $p)) { $mismatch += "$name (controller missing)" }
}
$mapperXml = Join-Path $dst 'mapper\serve\AppFoodMapper.xml'
if (-not (Test-Path $mapperXml)) { $mismatch += 'AppFoodMapper.xml (missing)' }

if ($failedFiles.Count -gt 0) {
    throw ("copy failed for " + $failedFiles.Count + " file(s), usually locked by the running JVM; restart backend and retry: " + ($failedFiles -join ', '))
}
if ($mismatch.Count -gt 0) {
    throw ("verification failed for " + $mismatch.Count + " item(s): " + ($mismatch -join ', '))
}

Write-Output ("javac ok: sources={0}" -f $srcs.Count)
Write-Output ("synced {0} files -> {1}" -f $copied, $dst)
Write-Output ("verify ok: all class/resource artifacts are in place")
