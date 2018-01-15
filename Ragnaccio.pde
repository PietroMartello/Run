class Ragnaccio extends Nemico{
  int realY, posizione, timer, base;  //Questo nemico saltella, queste variabili servono per gestire il salto
  // 1 -> salto
  // 2 -> atterraggio
  // 0 -> nulla
  Ragnaccio(){
    super(18,18,height-40,35,28,1);
    base = coorY;
    I[0] = loadImage("Ragnetto azzurro.png");
    realY = int(random(height-150, height-75));  // Questa variabile rappresenta la massima altezza raggiungibile dal salto.
    posizione = 0;  // Questa variabile indica se il ragno sta saltando(1), stando fermo(0) o cadendo(2).
    timer = int(random(0,100));   // Questa variabile indica il tempo fra un salto e l'altro. Inizializzata random ad ogni reset.
  }
  void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed;
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(timer == 0){  // Quando il timer arriva a 0 inizia il salto e il timer è re-inizializzato.
        timer = int(random(0,100));
        posizione = 1;
      }
      else if(posizione == 0){  // decrementa il timer
        timer--;
      }
      if(posizione == 1){  // Questi if gestiscono il salto in modo simile a quanto avviene nella classe Hero
        coorY-=3;
        if(coorY <= realY)  posizione = 2;
      }
      if(posizione == 2){
        coorY+=3;
        if(coorY >= base){
          coorY = base;
          posizione = 0;
        }
      }
      if(coll != 2){
        image(I[0],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      }
      else{
        coorY = height-40;
        image(I[0],coorX-lunIm,coorY+alt+5,lunIm*2,altIm/3);
      }
    }
    else{
      count--;
      if(count <= 0){
        reset();
        realY = int(random(height-150, height-75));
        timer = int(random(0,100));
      }
    }
    collisione(m.h);
  }
}
