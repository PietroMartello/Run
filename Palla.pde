class Palla extends Nemico{  // Un semplicissimo nemico mobile che dà tanta atmosfera.
  int ind,cont;  // Indicatore d'animazione
  Palla(){
    super(31,31,height-50,35,35,4);
    for(int i=0; i<4; i++){
      I[i] = loadImage("Palla di Polvere "+i+".png"); 
    }
    ind = cont = 0;
  }
  void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed+2;
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(coll != 2){
        image(I[ind],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
        cont++;
        if(cont == 5){
          ind = (++ind)%(I.length);
          cont = 0;
        }
      }
      else{
        coorX += 2;
        image(I[0],coorX-lunIm,coorY+alt+5,lunIm*2,altIm/3);
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
}
