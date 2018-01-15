class Robi extends Nemico{  // Nemico mobile appuntito di base
  int ind,cont;
  Robi(){
    super(25,14,height-50,32,40,3);
    I[2] = loadImage("Robi.png");
    for(int i=0; i<2; i++){
      I[i] = loadImage("Polvere "+(i+1)+".png");
    }
    ind = 0;
    cont = 0;
  }
  void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed+2;
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      image(I[2],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      image(I[ind],coorX+lunIm,coorY+altIm/2+5,16,9);
      cont++;
      if(cont == 5){
        ind = (++ind)%2;
        cont = 0;
      }
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
