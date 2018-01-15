class Ragnetto extends Nemico{
  int realY, tricky, vel;  // Questo nemico si lascia pendere dal soffitto e a volte casca, queste variabili gestiscono questo.
  Ragnetto(){
    super(16,18,20,35,28,1);
    I[0] = loadImage("Ragnetto verde.png");
    realY = int(random(height-200,height-55));  // Rappresenta il punto esatto dove il ragnetto resterà a pendere.
    tricky = int(random(1,5));   // Se tricky == 4 il ragnetto si lascia cadere ad una coordinata fissa (210)
    vel = 2;   // Velocità con cui il ragno si abbassa
  }
  void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed;
//      stroke(1);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(coorY < realY && coorX < width-50){  // Questo if gestisce il lento scendere del ragno.
        coorY = constrain(coorY+vel,0,realY);
      }
      if(tricky == 4 && coorX < width/2){   // Questo if gestisce la caduta
        realY =  height-30;
        tricky = 0;
        vel = 6;
      }
      if(coll != 2){
        stroke(2);
        if(tricky != 0)line(coorX,0,coorX,coorY);
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
        coorY = 0;
        realY = int(random(height-200,height-55));
        tricky = int(random(1,5));
        vel = 2;
      }
    }
    collisione(m.h);
  }
}
