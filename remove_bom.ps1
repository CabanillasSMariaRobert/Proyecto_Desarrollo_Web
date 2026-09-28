Get-ChildItem -Path src/main/java/com/ecommerce/tienda_kalza/servicios/publico/*.java | ForEach-Object {
    $content = [System.IO.File]::ReadAllText($_.FullName)
    [System.IO.File]::WriteAllText($_.FullName, $content, (New-Object System.Text.UTF8Encoding($false)))
}
