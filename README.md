# Boas práticas em animações com Jetpack Compose
## 1. Objetivo da pesquisa
* Estudar as melhores práticas em animações no Android
* Desenvolver uma animação Confetti conforme o [vídeo-exemplo](https://dribbble.com/shots/16973988-Perk-Hero-Level-Up-Animation) sugerido no desafio de apresentações semanais do grupo de estudos.
## 2. Como escolher a API correta para suas animações?
Lottie, SVGs animados ou animações composable? A documentação Android nos oferece um guia para escolher corretamente nossas APIs de animação. 

Quando queremos implementar uma animação em nosso aplicativo, temos de ver se **1)** a animação se assemelha mais com arte, contendo muitos elementos visuais ou imagens SVG? Se sim, devemos observar se **a)** a animação contém SVGs como ícones com micro animações? Caso for afirmativa, usamos o _AnimatedVectorDrawable_, animações padrão ou Composable, **caso contrário**, se a animação que se queira é rebuscada, com muitas imagens e detalhes, o mais indicado é utilizar um framework de animação como o Lottie.

![image](https://github.com/user-attachments/assets/13522a09-ff92-4288-bffd-56c5166b0f2b)
Referência: https://developer.android.com/develop/ui/compose/animation/choose-api

## 3. Primeira abordagem - Canvas e laços de repetição
Nesta abordagem o objetivo é desenvolver animações utilizando o Canvas (uma API de desenho de formas primitivas e pintura) junto com laços de repetição.
São criadas formas cujas propriedades se alteram de acordo com os laços: tamanho, cor, posição na tela e outros. Tudo é renderizado em tempo de execução.

Inicialmente é criado o modelo de confetti que terá as seguintes propriedades:
```kotlin
data class ConfettiParticle(
    var position: Offset,
    var velocity: Offset,
    var color: Color,
    var size: Float,
    var rotation: Float = 0f,
    var alpha: Float = 1f
)
```

Após isto, definimos o comportamento do confetti através dos seguintes métodos:
```kotlin
class ConfettiState {
    val particles: SnapshotStateList<ConfettiParticle> = mutableStateListOf()

    fun addParticle(
        position: Offset,
        color: Color,
        size: Float
    ) {
        val randomXVelocity = Random.nextFloat() * 20f - 10f
        val randomYVelocity = Random.nextFloat() * -30f - 10f
        val randomRotation = Random.nextFloat() * 360f

        particles.add(
            ConfettiParticle(
                position = position,
                velocity = Offset(randomXVelocity, randomYVelocity),
                color = color,
                size = size,
                rotation = randomRotation
            )
        )
    }

    fun updateParticles() {
        particles.removeAll { it.alpha <= 0f }
        particles.forEach { particle ->
            particle.position += particle.velocity
            particle.velocity = Offset(particle.velocity.x, particle.velocity.y + 0.5f)
            particle.alpha -= 0.01f
        }
    }
}
```
* `addParticle`: adiciona um novo confetti na lista de confettis a partir de posição, velocidade e rotação aleatórias.
* `updateParticle`: atualiza as propriedades do confetti como se fosse uma simulação física, removendo partículas invisíveis da lista de partículas e, para as partículas restantes, atualiza sua posição com base na velocidade atual, aumenta a velocidade simulando a gravidade e aumenta sua opacidade para que ela desapareça gradualmente.

Por fim:
* Aplicamos em nossa tela dentro de um _LaunchedEffect_ que criará novas partículas e as atualizará na lista a cada 50ms com propriedades aleatórias.
* Desenhamos o confetti através do Canvas para cada partícula da lista aplicando rotação na mesma.
```kotlin
@Composable
fun ConfettiAnimationCanvas(
    modifier: Modifier = Modifier,
    confettiState: ConfettiState = remember { ConfettiState() }
) {
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Magenta)

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(50)
            confettiState.addParticle(
                position = Offset(Random.nextFloat() * 1000f, 0f),
                color = colors.random(),
                size = Random.nextFloat() * 20f + 10f
            )
            confettiState.updateParticles()
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
    ) {
        confettiState.particles.forEach { particle ->
            rotate(degrees = particle.rotation, pivot = particle.position) {
                drawRect(
                    color = particle.color,
                    topLeft = Offset(particle.position.x - particle.size / 2, particle.position.y - particle.size / 2),
                    size = Size(particle.size, particle.size),
                    alpha = particle.alpha
                )
            }
        }
    }
}
```
### Resultado:
<img src="https://github.com/user-attachments/assets/4f0610be-a53e-4354-88e2-78c9c9a39c74" alt="Confetti Animation" width="300"/>

### Inspirações
* [Biblioteca Konfetti por Daniel Martinus](https://github.com/DanielMartinus/Konfetti)
* Estudo prático baseado no artigo de [Saravanai P. Ramanathan](https://medium.com/@saravanai.dev/jetpack-compose-confetti-7ad0629290fd)
## 4. Segunda abordagem - Animações Compose
Neste exemplo, foi animado uma box simples com três das propriedades que a animação precisaria ter: variações de cor, tamanho e visibilidade:
```kotlin
    var isBoxVisible by remember {
        mutableStateOf(true)
    }
    var animateBackgroundColor by remember {
        mutableStateOf(false)
    }
    var isBoxExpanded by remember {
        mutableStateOf(false)
    }

    val infiniteTransition = rememberInfiniteTransition()
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isBoxVisible) 1.0f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    val animatedColor by infiniteTransition.animateColor(
        initialValue = if (animateBackgroundColor) Color.Blue else Color.Red,
        targetValue = if (animateBackgroundColor) Color.Green else Color.Gray,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing,
                delayMillis = 1000
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    val animatedHeight by animateDpAsState(
        targetValue = if (isBoxExpanded) 100.dp else 50.dp,
        animationSpec = spring(
            stiffness = Spring.StiffnessHigh
        )
    )

    val animatedWidth by animateDpAsState(
        targetValue = if (isBoxExpanded) 100.dp else 50.dp,
        animationSpec = spring(
            stiffness = Spring.StiffnessHigh
        )
    )

    Box(
        modifier = Modifier
            .padding(bottom = 24.dp)
            .size(animatedHeight, animatedWidth)
            .graphicsLayer {
                alpha = animatedAlpha
            }
            .clip(RoundedCornerShape(16.dp))
            .background(animatedColor)
            .animateContentSize(
                animationSpec = tween(durationMillis = 1000)
)
    )
```
### Resultado
<img src="https://github.com/user-attachments/assets/4ba9883d-b4bd-4f26-b9a4-bbe30c935a9c" alt="Confetti Animation" width="300"/>

### Curiosidades
* O Compose usa por padrão animações **de mola**, ou animações baseadas em física, procure saber sonbre o [_animationSpec_](https://developer.android.com/develop/ui/compose/animation/customize?hl=pt-br#animationspec) usado nas animações acima, através dele pode-se personalizar o modo como a animação é executada.
* A documentação oficial apresenta um [Guia rápido sobre animações no Compose](https://developer.android.com/develop/ui/compose/animation/quick-guide?hl=pt-br) que pode ser muito útil para iniciar seus estudos em animações composable.
* A doc também disponibiliza um [Cheat Sheet de animações em Compose](http://developer.android.com/static/develop/ui/compose/images/compose_animation_cheat_sheet.png?hl=pt-br) que pode agilizar seu desenvolvimento.
## 5. Terceira abordagem - AnimatedVectorDrawable
AnimatedVectorDrawable, como o nome já sugere, aplica animações em vetores simples, basta carregar o arquivo drawable e alternar entre o estado final e inicia; do drawable:
```kotlin
@Composable
fun AnimatedVectorDrawable() {
    val image = AnimatedImageVector.animatedVectorResource(R.drawable.ic_hourglass_animated)
    var atEnd by remember { mutableStateOf(false) }
    Image(
        painter = rememberAnimatedVectorPainter(image, atEnd),
        contentDescription = "Timer",
        modifier = Modifier.clickable {
            atEnd = !atEnd
        },
        contentScale = ContentScale.Crop
    )
}
```
### Resultado
<img src="https://github.com/user-attachments/assets/25dc5bad-b83e-4346-8db0-491bd1b55a91" alt="Confetti Animation" width="300"/>

## 6. Quarta abordagem - Lottie
Uma biblioteca para exibição de animações vetoriais em aplicativos mobile e web, que permite renderizar animações criadas e exportadas no formato JSON.
Com ele, é possível adicionar animações leves e escaláveis sem comprometer o desempenho do app.

Para utilizar o Lottie, deve-se adicionar sua dependência:

**libs.versions.toml**
```kotlin
android-composeLottie = { group = "com.airbnb.android", name = "lottie-compose", version.ref = "composeLottie" }
```

**build.gradle.kts**
```kotlin
    implementation(libs.android.composeLottie)
```

**sua_tela.kt**
```kotlin
@Composable
fun Lottie() {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(
            resId = R.raw.confettis
        )
    )
    LottieAnimation(
        composition = composition,
        contentScale = ContentScale.Fit,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier
            .fillMaxWidth()
            .height(514.dp)
    )
}
```
### Resultado
<img src="https://github.com/user-attachments/assets/c0ff77f1-5052-4d7b-a671-699a2701f0d6" alt="Confetti Animation" width="300"/>

## 7. Conclusão - Impacto na performance com as animações
### 7.1. Animações com Canvas e laços de repetição
A criação e atualização frequente de novas partículas que representam o Confetti, podem impactar no processador e memória do dispositivo, ainda mais dispositivos menos potentes, pois:
* O código adiciona novas partículas a cada x milissegundos.
* Possível queda de FPS com a renderização contínua do Canvas.
* A cada frame o Canvas redesenha as partículas.
* Possível excesso de recomposições no Compose
### 7.2. Animações Compose
As animações no Compose podem causar problemas de desempenho por conta da própria natureza da animação, que é mover ou mudar pixels na tela rapidamente, frame por frame. 

Considerando as diferentes fases do Compose (composição, layout e renderização), se a animação mudar a fase de layout, todos os elementos combináveis afetados serão reprojetados e redesenhados. 

Se a animação ocorre na fase de renderização, então ela terá mais desempenho por padrão do que se ela fosse executada na fase de layout. 
Referência: https://developer.android.com/develop/ui/compose/animation/quick-guide?hl=pt-br#optimize-performance  
#### 7.2.1 Onde as animações compose melhor se adequam?
As animações do Compose desempenham um papel fundamental na melhoria da experiência do usuário em plataformas digitais. 

No entanto, são mais indicadas em guiar a atenção do usuário, sinalizar transições ou fornecer feedback sobre ações específicas, fazendo com que as interações pareçam fluidas e intuitivas. 

> _"Se as animações não forem cuidadosamente incorporadas, elas podem atrapalhar a experiência geral do usuário. Animações excessivamente complexas, longas ou redundantes podem se tornar fontes de distração e aborrecimento. Elas também podem contribuir para fazer com que um aplicativo pareça pesado ou lento, levando os usuários a potencialmente abandonar tarefas ou o aplicativo completamente."_

Referência: https://dev.to/andreytzkt/animations-in-jetpack-compose-evolution-performance-and-testing-3ol6  
### 7.3. Bibliotecas terceiras - Lottie
* **Ponto positivo:** Já estão renderizados, não seria necessária desenhar cada partícula dinamicamente, reduzindo uso do processamento e memória.
* **Ponto negativo:** Por outro lado, pode não ser a melhor opção se quiser interações mais complexas, como em tempo real e com uso de física ou colisões. 
