// Questa classe gestisce l'avatar del personaggio.
class Hero{
  float alt, lun, coorX, coorY, salto, altIm, lunIm, altsalto;  // coorX e coorY sono la posizione del personaggio, alt e lun sono i lati del rettangolo dell'area di collisione del protagonista, altIm e lunIm servono a dare le coordinate all'immagine
  // salto e altsalto servono a stabilire rispettivamente quando il personaggio tocca terra e qual'è la massima altezza che può raggiungere.
  PImage ar[];  // Tutte le immagini di cui ho bisogno.
  int posizione;  //Indica l'azione che sta compiendo
//  1 -> camminata
//  2 -> salto
//  3 -> atterraggio
//  4 -> calcio
//  5,6 -> scivolata 
  int colpito;  //Indica se è stato appena subito un danno e per quanto tempo se ne sarà immuni
  int cont, ind, scivol;  //Indicatori d'animazione
  float vita, stamina;  //Vita ed energia per compiere azioni.
  
  
  Hero(){
    alt = 60;
    lun = 22;
    altIm = 60;
    lunIm = 40;
    coorX = 70;
    salto = coorY = height - 76;
    altsalto = 150;
    posizione = 1;
    scivol = 1;
    colpito = 1;
    ar = new PImage[10]; // Caricamento delle immagini
    for(int i=0; i<10; i++){
      ar[i] = loadImage("Ninja "+(i+1)+".png");
    }
    cont = 0;
    ind = 0;
    vita = 5;
    stamina = 50;
  }
  
  
  void disegna(){ // Metodo richiamato ad ogni iterazione di draw. Disegna il personaggio e ne stabilisce l'azione a seconda dell'input di tastiera.
    
    // Interazioni con la testiera. Credo siano abbastanza esplicative.
    if(keyPressed){
      if(key == CODED){
        if(keyCode == UP){
          salta();
        }
      }
    }
    if(keyPressed){
      if(key == CODED){
        if(keyCode == DOWN){
          vaigiu();
        }
      }
    }
    if(posizione == 2){  // Vuol dire che sto saltando, quindi decremento coorY finché non arrivo al valore indicato da altsalto, poi cambio la posizione.
      coorY-=3;
      if(coorY <= salto-altsalto)  posizione = 3;
    }
    if(posizione == 3 || posizione == 4){  // Vuol dire che ho terminato il salto, quindi incremento coorY finché non torno a terra, poi ricomincio a correre (posizione = 1)
      coorY+=3;
      altsalto = 150;
      if(posizione == 4) coorY+=3;  // Se sto facendo un calcio volante cado più velocemente. Sono tecniche ninja avanzatissime.
      if(coorY >= salto){
        coorY = salto;
        posizione = 1;
      }
    }
    if(posizione == 5){  // Mi sto abbassando.
      abbassati();
    }
    if(posizione == 6 && m.stato != 0){  // Mi sto alzando, ma solo se non tengo la posizione continuando a premere il pulsante che mi sta facendo abbassare.
      if(!keyPressed){
        rialzati();
      }
    }
    // Questa parte commentata permette di visualizzare l'area di collisione dell'avatar o dei nemici. Solo nel caso di Hero è formata dall'intersezione di due rettangoli, non uno.
//    stroke(0);
//    fill(255,0);
//    rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
//      fill(0,0,255,128);
//      rect(coorX,coorY+alt,lun,alt);
    if(!(colpito%3==0)){  // Questo if permette al personaggio di lampeggiare per un po' quando viene colpito, finché non ritorna vulnerabile.
      if(posizione == 2 || posizione == 3){
        image(ar[7],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      }
      else if(posizione == 4){
        image(ar[6],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      }
      else if(posizione == 5 || posizione == 6){
        image(ar[8],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      }
      else{
        image(ar[ind],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      }
    }
    if(colpito > 1)  colpito--;  // Decrementa il periodo in cui l'eroe è invulnerabile subito dopo aver subito danni.
    if(posizione == 1) cont++;
    if(cont == 5){
      ind = (++ind)%(ar.length - 4);
      cont = 0;
    } // Questi ultimi due if regolano l'animazione. Una struttura simile si trova in ogni oggetto che prevede un'animazione. Scorre un'array di immagini o parte di esso visualizzandone una per poco tempo e poi passando alla successiva.
    
    
    displayVita();
    
  }
  
  
  void muori(){  // Viene richiamato se esaurisco la vita, mostra l'immagine del protagonista svenuto che scivola lentamente via.
    image(ar[9],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
    if(coorX >= -lunIm){
      cont++;
      if(cont%5 == 0){
        coorX -= scivol;
        if(cont%25 == 24) scivol = scivol*2; 
      }
    }
  }
  
  void reset(){   // Una copia del costruttore per resettare il gioco o le caratteristiche del personaggio
    alt = 60;
    lun = 22;
    altIm = 60;
    lunIm = 40;
    coorX = 70;
    salto = coorY = height - 76;
    altsalto = 150;
    posizione = 1;
    scivol = 1;
    colpito = 1;
    cont = 0;
    ind = 0;
    vita = 5;
    stamina = 50;
  }
  
  
  void displayVita(){  // Mostra la vita nell'angolo in alto a destra della finestra, oltre a ripristinare la stamina ad ogni chiamata.
    rectMode(CORNER);
    stroke(0,0);
    fill(255,0,0);
    rect(width-160,5,vita*30,15);
    fill(#11FF11);
    rect(width-160,20,stamina*2,15);
    if(stamina < 50 && posizione == 1 && cont == 0){
      stamina+=2;
    }
  }
  
  void salta(){  // Cambia la posizione del protagonista iniziando il salto.
    if(stamina >= 5){
      if(posizione == 1){
        posizione = 2;
        stamina-=5;
      }
      if(posizione == 5 || posizione == 6){
        rialzati();
        posizione = 2;
      }
    }
  }
  
  void abbassati(){  // Cambia l'area in cui il personaggio è vulnerabile e il punto dove si mettono le immagini quando sto scivolando.
    coorY = height - 38;
    float swap = alt;
    alt = lun;
    lun = swap;
    swap = altIm;
    altIm = alt+9;
    lunIm = lun+8;
    posizione = 6;
    stamina-=5;
  }
  
  void rialzati(){  // Ripristina l'area di collisione e le immagini quando finisco di scivolare.
    posizione = 1;
    coorY = height - 76;
    float swap = alt;
    alt = lun;
    lun = swap;
    altIm = alt;
    lunIm = lun+18;
  }
  
  void vaigiu(){  // In base alla situazione, permette di abbassarsi oppure di eseguire un calcio volante.
    if(posizione == 1 && stamina >= 5) posizione = 5;
    else if((posizione == 3 || posizione == 2) && stamina >= 5){
      posizione = 4;
      stamina-=5;
    }
  }
  
  void ferisci(){  // Viene richiamato da un oggetto derivato da nemico se colpisce il personaggio. Decrementa vita e decide cosa fare in base alla difficoltà.
    if(colpito == 1){  // Non viene attivato se il personaggio è stato colpito da poco da qualche altro nemico. Colpito viene inizializzato da ferisci e viene decrementato dal metodo disegna.
      colpito = 73;
      vita--;
      if(m.diff == 0)  m.colpito++;
      if(vita == 0){
        if(m.diff == 0) vita = 5;
        else{
          abbassati();
          m.stato = 0;
          m.pano.setGameOver();
        }
      }
    }
  }
  
  void cura(){  // Metodo esclusivo che viene richiamato solo da Pozione.
    if(vita < 5) vita++;
  }
}

