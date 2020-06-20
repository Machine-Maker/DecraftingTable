#Copy-Item -Path $args[0] -Destination $args[1]
Param (
    [Parameter(Mandatory=$True)]
    [string]$JarFile,

    [Parameter(Mandatory=$True)]
    [string]$OutputDir
)

Copy-Item -Path $JarFile -Destination $OutputDir
Write-Host "Successfully copied $JarFile to $OutputDir"