class Avvoltoio extends Nemico{  // Nemico volante avanzato
  int cont, ind; //Controllori per l'animazione
  int tetto, posizione, base;  //Questo nemico svolazza, ovvero va su e giù mentre si avvicina, queste variabili servono per gestire lo svolazzo
  // posizione = 1 -> sali
  // posizione = 2 -> scendi
  // tetto -> massima altezza raggiungibile
  // base -> minima altezza raggiungibile
  Avvoltoio(){
    super(34,15,int(random(height-200,height-55)),40,40,2);
    for(int i=0; i<2; i++)
      I[i] = loadImage("Avvoltoio "+(i+1)+".png");
    tetto = constrain(coorY-int(random(0,60)),50,220);
    posizione = int(random(1,3));
    base = constrain(coorY+int(random(0,60)),100,220);
    ind = cont = 0;
  }
  void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed*2+1;  // L'avvoltoio vola e va veloce.
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(posizione == 1){  // Si alza finché non arriva al tetto.
        coorY-=2;
        if(coorY <= tetto)  posizione = 2;
      }
      else if(posizione == 2){  // Si abbassa finché non arriva alla base
        coorY+=2;
        if(coorY >= base)  posizione = 1;
      }
      if(coll != 2){
        image(I[ind],coorX-lunIm,coorY-altIm-20,lunIm*2,altIm*2);
        cont++;
        if(cont == 6){
          ind = (++ind)%(I.length);
          cont = 0;
        }
      }
      else{
        coorY = height-40;
        image(I[0],coorX-lunIm,coorY+alt+5,lunIm*2,altIm/3);
      }
    }
    else{
      count--;
      if(count <= 0){
        reset();  // Le aggiunte a Reset servono a resettare parametri posseduti solo dal nemico in questione.
        coorY = int(random(height-200,height-55));
        tetto = constrain(coorY-int(random(10,50)),50,220);
        base = constrain(coorY+int(random(10,50)),100,220);
      }
    }
    collisione(m.h);
  }
}
