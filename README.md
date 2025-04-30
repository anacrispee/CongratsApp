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
Nesta abordagem o objetivo é desenvolver animações utilizando o Canvas, uma API de desenho de formas primitivas e pintura junto com laços de repetição. 
São criadas formas cujas propriedades se alteram de acordo com os laços: tamanho, cor, posição na tela e outros, tudo é renderizado em tempo de execução.
### Inspirações
* [Biblioteca Konfetti por Daniel Martinus](https://github.com/DanielMartinus/Konfetti)
* Estudo prático baseado no artigo de [Saravanai P. Ramanathan](https://medium.com/@saravanai.dev/jetpack-compose-confetti-7ad0629290fd)

Como foi feito no código:

Resultado:
<img src="https://github.com/user-attachments/assets/4f0610be-a53e-4354-88e2-78c9c9a39c74" alt="Confetti Animation" width="300"/>
