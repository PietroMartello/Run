// un gioco del 2015 di Pietro Martello
// Libreria per mettere la musica
import ddf.minim.spi.*;
import ddf.minim.signals.*;
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.ugens.*;
import ddf.minim.effects.*;

int sizealt = 300, sizelun = 700;  // Variabili per la misura della finestra, possono essere cambiate da qui.
Motore m;  // L'oggetto motore contiene tutte le altre classi e regola le varie fasi del gioco.
Minim minim;  // Oggetto di default della libreria sonora minim.
boolean pause;  // Boolean che permette di mettere in pausa il gioco in qualsiasi momento.


void setup(){
  size(sizelun,sizealt);
  minim = new Minim(this);  // Inizializzazione di Minim, vuole il path del programma.
  m = new Motore();  // Inizializzazione del motore, richiama a catena l'inizializzazione di tutti gli altri oggetti.
  pause = false;    // Il gioco non è in pausa all'inizio. 
}

void draw(){
  if(!pause) m.play();  // se il gioco non è in pausa, l'oggetto motore si occupa di tutto. Tranquillo.
}

void keyReleased(){  //Viene effettuato un controllo periodico sulla tastiera per decidere quando mettere in pausa.
  if(key == 'p'){
    pause = !pause;
    textSize(50);
    fill(255,0,0);
    text("PAUSA",width/2-100,height/2-25,200,50);
    textSize(25);
    text("Premi 'p' per ricominciare",width/2-160,height/2+25,320,25);
  }
  // Il blocco di codice mette o toglie la pausa, e inserisce la scritta che dice che il programma è in pausa.
  // Tale scritta viene inserita anche quando viene tolta la pausa, ma verrà immediatamente cancellata alla prossima iterazione di draw, ovvero appena il programma riprende.
}
