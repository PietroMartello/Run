class Pozione extends Nemico{  // Pozione non è un nemico, ma appare e funziona nello stesso modo, tranne che cura al posto di ferire.
  Pozione(){
    super(10,20,int(random(height-200,height-55)),13,24,1);
    I[0] = loadImage("Pozione.png");
    count = -1;
  }
  void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed;
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(coll == 0) image(I[0],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);;
    }
    else{
      if(m.punteggio == 2500 || m.punteggio == 7500 || m.punteggio == 12500 || m.punteggio == 17500)  count = int(random(0,500));  // Pozione appare soltanto in momenti prefissati
      if(count >= 0){
        count--;
        if(count == 0){
          reset();
        }
      }
    }
    collisione(m.h);
  }
  
  void reset(){  // Il metodo reset è modificato perché Pozione appare solo in certi momenti.
    coorX = sizelun + int(random(100,1000));
    coorY = int(random(height-200,height-55));
    coll = 0;
    count = -1;
  }
  
  void collisione(Hero h){  // Uguale a quello di un nemico appuntito, solo che cura al posto di ferire.
    float abbcoef;
    if(h.posizione == 6) abbcoef = 0;
    else                 abbcoef = h.alt;
    if(coorX-lun <= h.coorX+h.lun && coorX+lun >= h.coorX-h.lun
        && coorY-alt <= h.coorY+abbcoef && coorY+alt >= h.coorY-h.alt){
      if(coll == 0){
        h.cura();
        coll = 1;
      }
    }
  }
}
