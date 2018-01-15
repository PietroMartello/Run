class Mummia extends Nemico{
  int ind,cont; // Indicatore d'animazione
  int tele, trick;  // Questo nemico si teletrasporta, queste variabili servono a questo.
  Mummia(){
    super(22,31,int(random(height-300,height-55)),32,40,2);
    for(int i=0; i<2; i++){
      I[i] = loadImage("Mummy "+(i+1)+".png"); 
    }
    ind = cont = 0;
    trick = int(random(2,3));   // Mummia si teletrasporta se trick è uguale a 2. Ora come ora è sempre uguale a 2, perché ho pensato sarebbe stato più bello.
    tele = 36;
  }
  void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed;
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt); 
      if(coll != 2){
        if(tele%4 == 0)  image(I[ind],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
        cont++;
        if(cont == 7){
          ind = (++ind)%(I.length);
          cont = 0;
        }
      }
      else{
        coorY = height-60;
        image(I[0],coorX-lunIm,coorY+alt+5,lunIm*2,altIm/3);
      }
      if(trick == 2 && tele > 0 && coorX < width*2/3)  tele--; 
      else if(tele == 0){
        trick = 0;  // trick viene riassegnato ad 1 cosìcché non si teletrasporti più di una volta per apparizione.
        tele = 36;
        coorY = int(random(height-300,height-55));   // Il luogo in cui il nemico si trasporterà varia tra questi due valori.
      }
    }
    else{
      count--;
      if(count <= 0){
        reset();
        coorY = int(random(height-300,height-55));
        trick = int(random(2,3));
      }
    }
    collisione(m.h);
  }
}
