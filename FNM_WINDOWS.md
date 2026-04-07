## Windows で fnm を使って Node.js 20 をインストール・切り替える方法

### 1. fnm をインストール

```powershell
winget install Schniz.fnm
```

PowerShell を再起動後、確認：

```powershell
fnm --version
```

---

### 2. Node.js 20 をインストール

```powershell
fnm install 20
```

---

### 3. PowerShell　で　profile を設定

```powershell
if (-not (Test-Path $profile)) { New-Item $profile -Force }

Invoke-Item $profile
```

profile に以下を追加：

```powershell
fnm env --use-on-cd --shell powershell | Out-String | Invoke-Expression
```

PowerShell を **再起動**。


> **Profileエラーが発生した場合**、上記の代わりに以下を profile に切り替え対応で追加してください：
>
> ```powershell
> # fnm setup
> $env:FNM_MULTISHELL_PATH = "$env:TEMP\fnm_multishell_$(New-Guid)"
> New-Item -ItemType Directory -Force -Path $env:FNM_MULTISHELL_PATH | Out-Null
> fnm env --use-on-cd --shell powershell --multi | ForEach-Object {
>     if ($_ -match '^\$env:(\w+)\s*=\s*"(.*)"') {
>         Set-Item "env:$($Matches[1])" $Matches[2]
>     } elseif ($_ -notmatch '^function|^#') {
>         # function以外のシンプルな行はそのまま実行
>         try { Invoke-Expression $_ } catch {}
>     }
> }
> ```


PowerShell を **再起動**。


---

### 4. Node.js 20 を有効化

```powershell
fnm use 20
node -v
```

`v20.x.x` が表示されれば完了。
