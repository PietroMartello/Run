// Popponaso è un nemico speciale formato dall'unione indissolubile tra due entità, il Poppo e il Naso.

class Naso extends Nemico{
  int start;  // Questa variabile indica quando il naso si stacca e comincia a volare per conto suo.
  int cont, ind;  // Indicatore d'animazione
  Naso(int cY){  // Il naso viene inizializzato nella giusta posizione per apparire attaccato al Poppo, poi si discosta quando start < di un valore random.
    super(17,16,cY+1,19,18,3);
    I[0] = loadImage("Naso.png");
    for(int i=1; i<3; i++){
      I[i] = loadImage("Fuoco "+i+".png");
    }
    start = width-int(random(100,500));
    ind = cont = 0;
  }
  void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed;
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(coorX < start){
        coorX -= m.speed;
        if(coll != 2)  image(I[ind+1],coorX+lunIm/2,coorY-altIm,lunIm*2,altIm*2);  // Animazione della fiamma
      }
      if(coll != 2)  image(I[0],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      //Il Naso non richiama mai esplicitamente il suo metodo reset, perché è il Poppo che la chiama per lui.
      cont++;
      if(cont == 5){
        ind = (++ind)%(I.length-1);
        cont = 0;
      }
      
      collisione(m.h);
    }
  }
  void reset(int coor){  // Nel metodo reset devo passare la nuova coordinata del Poppo, in modo che il naso possa essere risistemato a dovere.
    coorX = coor;
    coll = 0;
    start = width-int(random(100,500));
  }
}

class Popponaso extends Nemico{  // Il Popponaso è uguale in tutto e per tutto al Poppo, ma contiene al suo interno un naso mobile, che viene inizializzato e controllato da lui.
  Naso n;
  Popponaso(){
    super(25,26,height-50,42,43,1);
    I[0] = loadImage("Popponaso.png");
    n = new Naso(coorY);
  }
  void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed;
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(coll != 2){
        image(I[0],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      }
      else{
        image(I[0],coorX-lunIm,coorY+alt+5,lunIm*2,altIm/3);
      }
      n.disegna();
    }
    else{
      count--;
      if(count <= 0){
        reset();
      }
    }
    collisione(m.h);
  }
  
  void reset(){
    coorX = width + int(random(0,1000));
    coll = 0;
    count = int(random(0,250));
    n.reset(coorX-13);
  }
}
