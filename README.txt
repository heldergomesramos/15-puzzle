COMO COMPILAR:

Visto que os ficheiros são da linguagem java, deve ter o compilador de java instalado no seu computador (open jdk, "javac"),
a versão utilizada por nossa parte foi o OpenJDK 11.0.14, mas qualquer versão mais recente também deve funcionar (pode verificar a sua
executando java --version na consola).

De seguida, para compilar o código basta correr o comando $javac Jogo.java && javac JogoDosQuinzeMain.java
Não é necessário integrar nenhuma biblioteca especial, visto que o java trata disso internamente e apenas usamos
bibliotecas do javadoc.

Após compilar o código deve verificar que foram criados os ficheiros Jogo.class e JogoDosQuinzeMain.class. Agora
apenas precisa de correr o ficheiro principal na consola usando $java JogoDosQuinzeMain. Depois deve inserir o conjunto
de números que define o estado inicial do jogo, por ordem de entrada seguido do conjunto de números do estado final.
diretamente na consola como input.
Ex:
1 2 3 4 5 6 8 12 13 9 0 7 14 11 10 15
1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 0

Opcionalmente pode dar "pipe" de um ficheiro txt com o input desejado 
usando $java JogoDosQuinzeMain < input.txt, para facilitar o processo. De seguida irá aparecer na consola informação 
sobre a execução de todos os algoritmos, junto com o respetivo caminho para obter a solução desejada em cada um.

AMBIENTE DE COMPILAÇÃO 

O programa foi compilado por nossa parte usando o sistema operativo Linux, distro Kubuntu (basicamente Ubuntu mas com
uma GUI diferente) 20.04.3 LTS. Os ficheiros foram compilados usando o openJDK (java development kit) 11.0.14.

FEITO POR:
Duarte Monteiro
Hélder Ramos
Miguel Oliveira
