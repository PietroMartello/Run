class Ragno extends Nemico{  // Nemico veloce ma vulnerabile. Come il Poppo ma va più veloce ed è un po' più grande.
  int cont, ind; //Indicatori d'animazione
  Ragno(){
    super(31,22,height-50,40,33,2);
    for(int i=0; i<2; i++){
      I[i] = loadImage("Ragno "+(i+1)+".png");
    }
    ind = 0;
  }
  void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed*2;
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(coll != 2){
        image(I[ind],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      }
      else{
        coorX += m.speed;
        image(I[1],coorX-lunIm,coorY+alt+5,lunIm*2,altIm/3);
      }
    }
    else{
      count--;
      if(count <= 0){
        reset();
      }
    }
    cont++;
    if(cont == 6){
      ind = (++ind)%(I.length);
      cont = 0;
    }
    collisione(m.h);
  }
}
