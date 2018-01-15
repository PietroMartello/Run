class Johnny extends Nemico{  // Nemico spinoso piuttosto altino.
  Johnny(){
    super(23,20,height-50,39,35,1);
    I[0] = loadImage("Johnny.png");
  }
  void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed;
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      image(I[0],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);;
    }
    else{
      count--;
      if(count <= 0){
        reset();
      }
    }
    collisione(m.h);
  }
  void collisione(Hero h){  // Questo nemico fa l'overwriting di collisione perché non può essere sconfitto, ma danneggia sempre il personaggio.
    float abbcoef;
    if(h.posizione == 6) abbcoef = 0;
    else                 abbcoef = h.alt;
    if(coorX-lun <= h.coorX+h.lun && coorX+lun >= h.coorX-h.lun
        && coorY-alt <= h.coorY+abbcoef && coorY+alt >= h.coorY-h.alt){
      h.ferisci();
      coll = 1;
    }
  }
}
