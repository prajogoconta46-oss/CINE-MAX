# CINE MAX 🎬

Aplicativo Android de streaming com identidade premium em preto e dourado.

## Recursos iniciais
- Catálogo demonstrativo de filmes
- Categorias por gênero e busca
- Detalhes e lista de favoritos
- Reprodução de vídeo de demonstração com ExoPlayer (Media3)
- Interface Android nativa com Jetpack Compose
- GitHub Actions para compilar APK de depuração automaticamente

## Gerar o APK
1. Abra a aba **Actions** deste repositório.
2. Execute **Build CINE MAX APK** (ou envie uma alteração para a branch principal).
3. Abra a execução concluída e baixe o artefato **CINE-MAX-debug-apk**.
4. Extraia o ZIP e instale o arquivo APK em um Android compatível.

Também é possível abrir o projeto no Android Studio e executar **Build > Build APK(s)**.

## Streaming e direitos
O aplicativo vem com catálogo fictício e um vídeo público de demonstração para testar o player. Para disponibilizar filmes completos, conecte um catálogo e URLs de vídeo que você possua ou tenha autorização para distribuir. Não inclui filmes comerciais nem fontes piratas.

## Tecnologias
Kotlin, Jetpack Compose, Material 3, AndroidX Media3 (ExoPlayer), Gradle.
