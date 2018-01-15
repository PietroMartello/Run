class Disco extends Nemico{
  int vel, tricky;  // Questo nemico accelera in modo randomico
  Disco(int speed){
    super(33,15,int(random(height-200,height-55)),37,19,1);
    I[0] = loadImage("Disco.png");
    vel = speed;
    tricky = 1;  // L'accelerazione o la decelerazione viene effettuata quando questa variabile arriva a 0.
  }
  void disegna(){
    if(coorX >= -lun){
      coorX -= vel;
      tricky--;
      if(tricky == 0){
        if(m.diff == 2)  vel = int(random(m.speed-1,m.speed*3));
        else           vel = int(random(m.speed,m.speed*3));
        tricky = int(random(1,100));
      }
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(coll != 2)  image(I[0],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      else{
        coorY = height-40;
        image(I[0],coorX-lunIm,coorY+alt+5,lunIm*2,altIm/3);
      }
    }
    else{
      count--;
      if(count <= 0){
        reset();
        coorY=int(random(height-200,height-55));
      }
    }
    collisione(m.h);
  }
}
