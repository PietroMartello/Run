class Missile extends Nemico{   // Nemico volante di base
  int cont, ind; //Indicatori d'animazione
  Missile(){
    super(33,14,int(random(height-200,height-55)),37,17,4);
    for(int i=0; i<3; i++){
      I[i] = loadImage("Missile "+(i+1)+".png");
    }
    I[3] = loadImage("Esplosione.png");
    ind = cont = 0;
  }
  void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed*2;
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(coll != 2) image(I[ind],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      else{
        coorX += m.speed;
        image(I[3],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      }
    }
    else{
      count--;
      if(count <= 0){
        reset();
        coorY=int(random(height-200,height-55));
      }
    }
    cont++;
    if(cont == 5){
      ind = (++ind)%(I.length-1);
      cont = 0;
    }
    collisione(m.h);
  }
}
