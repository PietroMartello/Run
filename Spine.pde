class Spine extends Nemico{  // Un nemico speciale che ti colpisce solo quando tocchi terra
  Spine(){
    super(36,24,height-40,36,24,1);
    I[0] = loadImage("Spine.png");
  }
  
  void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed;  // Così ci si avvicina al protagonista.
//      stroke(0);
//     fill(255,0,0);
//     rectMode(RADIUS);
//     rect(coorX,coorY,lun,alt);
      image(I[0],coorX-lunIm,coorY+10-altIm,lunIm*2,altIm*2);
    }
    else{  // Qui avviene il decremento di count e il reset quando l'oggetto è fuori dalla vista del ninja.
      count--;
      if(count <= 0){
        reset();
      }
    }
    collisione(m.h);
  }
  
  void collisione(Hero h){  // Questo metodo collisione particolare è venuto fuori mentre testavo il metodo collisione è l'ho trovato utile. Funziona solo quando l'area di collisione del personaggio è sullo stesso livello di quella del nemico. 
    if(coorX-lun <= h.coorX+h.lun && coorX+lun >= h.coorX-h.lun && coorY+alt <= h.coorY+h.alt+1){
      h.ferisci();
    }
    // Le spine sono appuntite e non possono essere distrutte.
  }
}
