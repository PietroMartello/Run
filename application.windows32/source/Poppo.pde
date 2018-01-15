class Poppo extends Nemico{   // Nemico terrestre di base
  Poppo(){
    super(25,26,height-55,42,43,2);
    for(int i=0; i<2; i++){
      I[i] = loadImage("Poppo"+(i+1)+".png"); 
    }
  }
  void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed; // Così ci si avvicina al protagonista.
//      stroke(0);      // Questa parte commentata in ogni metodo draw permette di visualizzare l'area di collisione dei nemici
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(coorX-lun <= m.h.coorX+m.h.lun && coll == 0)  
        image(I[1],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);  // Il poppo se non lo schiacci ti guarderà sognante.
      else if(coll != 2){
        image(I[0],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      }
      else{
        image(I[0],coorX-lunIm,coorY+alt+5,lunIm*2,altIm/3);    // Un po' tutti i nemici vulnerabili vengono spiaccicati quando colpiti.
      }
    }
    else{  // Qui avviene il decremento di count e il reset quando l'oggetto è fuori dalla vista del ninja.
      count--;
      if(count <= 0){
        reset();
      }
    }
    collisione(m.h);
  }
  // Nota bene: Tutti i metodi disegna sono organizzati in modo simile a questo.
}
