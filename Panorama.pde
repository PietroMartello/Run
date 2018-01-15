// La classe Panorama gestisce gli sfondi e la musica.
class Panorama{
  PImage sfondo[], prec, spec[];  // Array degli sfondi, immagine ausiliaria, immagine speciale
  int ind, pos, t, num;  // indicatore dello sfondo, altre variabili necessarie
  int ind2, cont;  // variabili per gestire il quarto livello, in cui il personaggio distrugge il tessuto stesso della realtà.
  int end;  // Variabile che serve per suonare la musichetta del game-over, la splendida "A Warm Place" dei Nine Inch Nails, e resettarla quando ricomincia il gioco.
  AudioPlayer play[];  //MUSICA!!!!11!1
  boolean cambio;  // boolean che interrompe il livello in corso per iniziare il successivo.
  
  Panorama(){
    ind = 0;
    pos = 0;
    num = -5;
    t = 255;
    ind2 = 0;
    cont = 0;
    sfondo = new PImage[6];  // Caricamento degli sfondi.
    sfondo[0] = loadImage("Scan52.png"); // Menù Principale
    sfondo[1] = loadImage("Scan52.png"); // Livello 1
    sfondo[2] = loadImage("Scan53.png"); // Livello 2
    sfondo[3] = loadImage("Scan54.png"); // Livello 3
    sfondo[4] = loadImage("Blank.png");  // Livello 4
    sfondo[5] = loadImage("Blank.png");  // Crediti
    spec = new PImage[10];  // Immagini aggiuntive per il livello 4.
    spec[0] = loadImage("Les.png");
    spec[1] = loadImage("I&M.jpg");
    spec[2] = loadImage("Lallario.jpg");
    spec[3] = loadImage("Glitter.png");
    spec[4] = loadImage("Puglia 236.JPG");
    spec[5] = loadImage("SAM_0144.JPG");
    spec[6] = loadImage("Java.png");
    spec[7] = loadImage("Mac.jpg");
    spec[8] = loadImage("scimmia.png");
    spec[9] = loadImage("Musco.JPG");
    for(int j=0; j<4; j++){
      sfondo[j].resize(sfondo[j].width-1000,height);  // Ridimensiona gli sfondi per rendergli adattabili alla finestra
    }
    play = new AudioPlayer[7];  // Caricamento della musica.
    play[0] = minim.loadFile("Dust Bowl.mp3",2048);  // Menù Principale.
    play[1] = minim.loadFile("Oh, My Stars.mp3",2048);  // Livelli dall'1 al 4
    play[2] = minim.loadFile("Once In A Lifetime.mp3",2048);
    play[3] = minim.loadFile("Epic.mp3",2048);
    play[4] = minim.loadFile("Crank Heart.mp3",2048);
    play[5] = minim.loadFile("I Nearly Married A Human.mp3",2048);  // Bah e pensare che invitano a Sanremo gli Spandau Ballet e di lui non si ricorda più nessuno. Va Bé questi sono i crediti.
    play[6] = minim.loadFile("A Warm Place.mp3",2048);  // Musica del Game Over
    play[0].loop();  // Manda in loop la prima canzone
  }
  
  
  void scorri(){  // Questo menù serve per far scorrere lo sfondo. In realtà il protagonista non si muove, è lo sfondo a farlo creando l'illusione del movimento.
    tint(t);  // Il valore t viene cambiato dal metodo cambia per creare una dissolvenza tra i livelli.
    image(sfondo[ind],pos,0);  // Sistema lo sfondo. La variabile pos, settata a 0, viene diminuita facendo scorrere l'immagine verso sinistra.
    if(pos <= -sfondo[ind].width+width+5){  // Quando non è più possibile spostare lo sfondo viene segnalato. Il +5 c'è perché durante lo scan le immagini sono venute di poco ruotate e ho dovuto sistemarle al computer. In questo modo si maschera il risultato.
      pos = width;    // pos viene sistemata in modo da ricominciare a stampare l'immagine dall'inizio quando essa finisce.
      prec = get(0,0,width,height);  // Si usa un'immagine ausiliaria per evitare che quella vecchia resti ferma.
    }
    if(pos > 0){
      image(prec,pos-width,0);  // Finché non si è nella posizione originale, l'immagine prec scorre insieme a sfondo.
    }
    if(ind == 4 && !cambio){   // Nel quarto livello non c'è sfondo (in realtà c'è solo per evitare di dover creare un metodo a parte per questo livello), ma ci sono delle immagini che vengono visualizzate in modo inquietante.
        tint(#BBFFFF,50);
        image(spec[ind2],0,0,width,height);
        cont++;
        if(cont == 50){
        ind2 = (++ind2)%(spec.length);
        cont = 0;
        }
    }
    pos-=m.speed;  // diminuisce pos del valore speed.
    noTint();
    if(cambio){
      cambia();  // Una serie d'operazione innescate dal metodo cambiaLivello che, ecco, cambieranno il livello.
    }
  }
  
  
  void cambiaLivello(){  // Questo metodo viene richiamato dal motore. Inizia il processo di dissolvenza che cambierà il livello settando la variabile booleana corrispondente e fermando la musica.
    cambio = true;
    play[ind].pause();
    play[ind].rewind();
  }
  
  
  void cambia(){
    t = constrain(t+num,0,255);
      if(t==0){
        ind = (++ind)%sfondo.length;
        if(ind == 5) end = ind;
        num = 5;
        pos = 0;
      }
      if(t==255){
        num = -5;
        cambio = false;
        play[ind].loop();
        m.resetEnemies();
      }
  }
  
  void setGameOver(){  // Richiamato quando il personaggio muore, blocca la musica del livello e fa partire quella del Game Over.
    play[ind].pause();
    play[ind].rewind();
    end = 6;
    play[end].play();
  }
  
  
  void setNewGame(){  // Ruchiamato sia dopo il game over che alla fine dei crediti, resetta il contatore ind che indicava il livello e blocca la musica corrispondente.
    play[end].pause();  // end può essere uguale a 6, se ho perso, o a 5, se ho vinto.
    play[end].rewind();
    ind = 0;
    play[ind].loop();
  }
  
  int whereAreWeNow(){  // Dice in che livello ci troviamo
    return ind;
  }
  
  boolean getCambio(){  // Dice se siamo in una fase di transizione.
    return cambio;
  }
  
}

