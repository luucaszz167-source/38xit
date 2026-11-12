# 38HX PANEL

Projeto Android de painel flutuante demonstrativo, com interface preta e azul.

## Gerar APK pelo GitHub Actions
1. Crie um repositório GitHub e envie todo o conteúdo desta pasta.
2. Abra a aba **Actions**.
3. Selecione **Android Build** e execute **Run workflow**.
4. Quando terminar, abra a execução e baixe o artefato `38HX-PANEL-debug`.
5. Extraia o arquivo ZIP do artefato para obter `app-debug.apk`.

## Gerar localmente
Com JDK 17 e Gradle instalado:
```bash
gradle :app:assembleDebug
```
O APK ficará em `app/build/outputs/apk/debug/app-debug.apk`.

## Observação
O aplicativo implementa somente a janela flutuante e controles demonstrativos. Não acessa nem modifica a memória do Free Fire e não implementa aimbot ou ESP real.
