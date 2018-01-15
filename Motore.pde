// La classe motore contiene tutte le classi e i metodi creati da me per questo gioco.
class Motore{
  int speed;  // Velocità con cui scorre lo sfondo e i nemici vanno addosso al giocatore. Conservandola qua può essere cambiata durante il gioco, anche se non l'ho mai fatto avvenire.
  int stato, narrator, diff, punteggio, credits, creditime, colpito;  // Lo stato indica in quale momento del gioco ci si trova. All'interno del metodo play si trova una chiara suddivisione.
  // narrator viene utilizzato nel menù principale per permettere la navigazione.
  // Il punteggio viene incrementato automaticamente ogni iterazione di draw e raggiunta una certa soglia permette di passare di livello
  // diff indica difficoltà. Viene scelta nel menù principale e ha varie implicazioni:
  // n00b -> diff = 0. God Mode. Il personaggio non può essere ucciso. Questo permette di fare pratica e di visualizzare tutto il gioco in una volta. Non è molto bello dal punto di vista della giocabilità, ma l'ho inserito per lei, prof. Questo livello non permette di accedere al vero finale
  // ninja -> diff = 1. Difficoltà normale. Altro -> diff = 2. Difficoltà elevata. Nemici aggiuntivi e più forti.
  // credits e creditime sono due variabili per gestire i crediti finali.
  // Diversa dalla variabile con lo stesso nome in Hero, viene incrementata ogni volta che il personaggio viene colpito durante la God Mode.
  PFont font, gameo;  // Due font molto belli.
  PImage titolo;   // Non ho trovato un font abbastanza grande per il titolo, quindi l'ho disegnato io.
  
  Hero h;  // Oggetto contenente tutte le informazioni riguardo all'avatar del giocatore.
  Nemico nemici[];  // Collezione di oggetti derivati dalla classe nemici.
  Panorama pano;   // Oggetto contenente tutte le informazioni riguardanti sfondo e musica.
  
  Motore(){
    pano = new Panorama();  // Richiamo il metodo costruttore di Panorama.
    h = new Hero();  // Richiamo il costruttore di Hero. Tutti questi costruttori devono essere richiamati nel setup a cascata perché caricano delle immagini.
    speed = 3;
    nemici = new Nemico[15];  // Dopo aver deciso quanti nemici voglio, vengono creati. I costruttori a cui passo "speed" necessitavano tale informazione nel costruttore.
    nemici[0] = new Spine();
    nemici[1] = new Poppo();
    nemici[2] = new Missile();
    nemici[3] = new Ragnetto();  // Per quante volte possano comparire, c'è sempre solo un oggetto che rappresenta quel nemico, a meno che non ne possano comparire più d'uno nello schermo allo stesso momento.
    nemici[4] = new Ragnetto();
    nemici[5] = new Robi();
    nemici[6] = new Ragno();
    nemici[7] = new Ragnaccio();
    nemici[8] = new Avvoltoio();
    nemici[9] = new Palla();
    nemici[10] = new Mummia();
    nemici[11] = new Disco(speed);
    nemici[12] = new Johnny();
    nemici[13] = new Popponaso();
    nemici[14] = new Pozione();
    stato = 1;
    narrator = 0;
    diff = 1;
    punteggio = 0;
    colpito = 0;
    credits = height;
    creditime = 0;
    font = loadFont("KristenITC-Regular-48.vlw");
    gameo = loadFont("Chiller-Regular-72.vlw");
    titolo = loadImage("Titolo.png");
  }
  
  void play(){  // Il metodo play decide cosa deve apparire nello schermo in un certo momento e cosa no.
    if(stato == 2){
      // equivale al gioco in sé
      pano.scorri();  // Fa muovere il panorama.
      if(diff == 2 || pano.whereAreWeNow() != 4) nemici[0].disegna();  // Questo nemico è disegnato prima di Hero perché è l'unico che, durante il contatto, non viene disegnato sull'avatar.
      h.disegna();  // Fa compiere un movimento all'avatar.
      if(diff != 0)  nemici[14].disegna();  // Questo nemico è disegnato a parte perché si tratta della pozione che riempie la salute del protagonista. Era talmente simile ad un nemico da non aver senso gestirla come classe a parte.
      commandEnemies();  // Questo metodo fa compiere dei movimenti ai nemici.
      if(!pano.getCambio()) punteggio++;  // Aumento il punteggio quando sto giocando e il livello non sta cambiando
      if(punteggio == 5000 || punteggio == 10000 || punteggio == 15000){  
        pano.cambiaLivello();  // Passa al livello successivo ai punteggi sopracitati. 
      }
      if(punteggio == 20000){
        stato = 3;
        pano.cambiaLivello();  // Il quarto è l'ultimo livello, pertanto c'è anche bisogno di uscire dallo stato 2.
      }
    }
    else if(stato == 0){
      // equivale al Game Over
      pano.scorri();        // il panorama continua a muoversi, per un uscita drammatica.
      nemici[0].disegna();  // Anche i nemici funzionano come al solito.
      h.muori();            // Solo il personaggio ha un'animazione diversa.
      commandEnemies();
      displayGameOver();    // Mostra la scritta Game Over e permette di tornare al menù principale.
    }
    else if(stato == 1){
      // Menù Principale
      pano.scorri();       // il panorama si muove
      h.disegna();         // anche il personaggio, e può fare qualsiasi mossa presente nel gioco.
      if(narrator == 0)  displayMenu();  // Se Narrator = 0 sono nel menù principale. Da qui cambio difficoltà o inizio a giocare.
      else if(narrator == 1)  displayInstruction();  // Altrimenti mostro le istruzioni e la storia dietro al gioco.
      // narrator poteva essere un boolean, ma ci potrebbe essere la necessità di aggiungere altre opzioni disponibili.
    }
    else if(stato == 3){
      //VITTORIA!
      pano.scorri();
      h.disegna();
      displayCredits();  // Titoli di coda.
    }
  }
  
  void displayMenu(){
    color c1, c2, c3, d1, d2, d3;  // Colori che indicano come colorare il testo. Cambiano a seconda della posizione del mouse.
    int startX = 220, startY = 55, ingombroX = 195, ingombroY = 55;  // Queste variabili indicano dove posizionare il testo.
    float button1X= startX+ingombroX/1.5+20, button2X= startX+ingombroX+35, button1Y= startY+ingombroY*2, button2Y= startY+ingombroY*2;
    c1 = c2 = c3 = d1 = d2 = d3 = color(0,0,0);
    if(diff == 0)       d1 = color(255,0,0);
    else if(diff == 1)  d2 = color(255,0,0);
    else                d3 = color(255,0,0);
//     GIOCA --> va al gioco
    if(mouseX > startX && mouseX < startX+ingombroX && mouseY > startY && mouseY < startY+ingombroY){  // Ognuno di questi if dice cosa fare quando il mouse passa sopra una scritta.
      c1 = color(0,255,0);
      if(mousePressed){  // Questi if interni dicono cosa fare quando si clicca sulla scritta corrispondente.
        stato = 2;
        pano.cambiaLivello();
      }
    }
//      ISTRUZIONI --> va al metodo successivo
    else if(mouseX > startX+10 && mouseX < startX+ingombroX+5 && mouseY > startY+ingombroY && mouseY < startY+ingombroY*2-15){
      c2 = color(0,0,255);
      if(mousePressed)  narrator = 1;
    }
//     Bottoni della difficoltà
 /* else if(mouseX > startX+ingombroX/1.5+15 && mouseX < startX+ingombroX/1.5+15+ingombroX/3 && mouseY > startY+ingombroY*2 && mouseY < startY+ingombroY*2+26){
      if(mousePressed){
        diff = 0;
      }
    }*/
    else if(mouseX > button1X && mouseX < button1X+ingombroX/3 && mouseY > button1Y && mouseY < button1Y+26){
      if(mousePressed){
        diff = 1;
      }
    }
    else if(mouseX > button2X && mouseX < button2X+ingombroX*4/5 && mouseY > button2Y && mouseY < button2Y+26){
      if(mousePressed){
        diff = 2;
      }
    }
    // Tutto il testo che viene visualizzato, con le dimensioni e il colore di ciascuno.
    textFont(font);
    image(titolo,startX+ingombroX+20,startY-20,230,120);  // Logo del gioco.
    textSize(50);
    fill(c1);
    text("GIOCA",startX,startY,ingombroX,ingombroY);
    textSize(36);
    fill(c2);
    text("Istruzioni",startX+10,startY+ingombroY+5,ingombroX-5,ingombroY-15);
    textSize(26);
    
    
    fill(255,0,0);
    text("difficoltà:",startX+10,startY+ingombroY*2,ingombroX/1.5,26);
    //fill(d1);
    //text("noob",startX+ingombroX/1.5+15,startY+ingombroY*2,ingombroX/3,26);
    fill(d2);
    //text("ninja",startX+ingombroX+30,startY+ingombroY*2,ingombroX/3,26);
    text("ninja",button1X,button1Y,ingombroX/3,26);
    //textSize(15);
    fill(d3);
    //text("come in quel sogno in cui sei nudo al liceo, interrogato di latino",startX+ingombroX,startY+ingombroY*2+30,ingombroX*1.3,35);
    text("mega-ninja",button2X,button2Y,ingombroX*4/5,26);
  }
  
  void displayInstruction(){
    // Questo metodo funziona esattamente come quello precedente.
     int startX = 200, startY = 60, ingombroX = 500, ingombroY = 200;
     textSize(30);
     fill(0);
     text("Istruzioni",startX,startY-33,ingombroX,30);
     String s = "Era un giorno bello e assolato. "+
                "Ninjpg decise di uscire per una corsetta."+
                "Sfortunatamente, il nostro eroe sapeva che dovunque poteva celarsi un pericolo. "+
                "Ninjpg doveva guardarsi le spalle.\n"+
                "- Premi la freccetta in alto per saltare.\n"+
                "- Tieni premuta la freccetta in basso per abbassarti.\n"+
                "- Premi la freccetta in basso mentre salti per dare un calcio volante.\n"+
                "  Se atterri su un nemico non appuntito con un calcio volante salterai ancora!\n"+
                "- Ogni azione consuma la barretta verde, se finisce dovrai aspettare che si\n"+
                "  ricarichi\n"+
                "- Se vieni colpito perderai vita (la barretta rossa), se la perdi tutta\n"+
                "  perderai (ma che caspita te lo dico a fare?)."+
                "<-- Puoi farlo ora! Guarda!";
     textSize(14);
     fill(255,30,30);
     text(s,startX,startY,ingombroX,ingombroY);
     color c = color(0,0,255);
     // Bottone per tornare indietro.
     if(mouseX > startX && mouseX < startX+75 && mouseY > startY+ingombroY+5 && mouseY < startY+ingombroY+25){
       c = color(255);
       if(mousePressed)  narrator = 0;
     }
     textSize(18);
     fill(c);
     text("Indietro",startX,startY+ingombroY+5,75,20);
  }
  void displayCredits(){  // Questo metodo viene richiamato alla fine per mostrare i titoli di coda, come nel precedente, i titoli di coda sono scritti ordinati come somma di stringhe.
    color c = color(#8E7C5E);
    int startX = 10, startY = height-50,ingombroX = 140;
    //  Bottone che permette di tornare al menù principale.
    if(mouseX > startX && mouseX < startX+ingombroX && mouseY > startY && mouseY < startY+17){
       c = color(0,255,0);
       if(mousePressed){
         stato = 1;
         punteggio = 0;
         pano.setNewGame();
         h.reset();
         resetEnemies();
         credits = height;
       }
     }
    String s = "Ed è così che andò.\n\n"+
               "Ninjpg corse a lungo, così tanto da superare i limiti della realtà stessa.\n\n"+
               "Da quel giorno, nessuno ebbe mai più notizie di lui.\n\n\n"+
               "Ma c'è chi dice\n\n"+
               "che Ninjpg stia ancora correndo, portando il suo aiuto dovunque sia necessario.\n\n\n"+
               "E così farà, fino alla fine dei tempi.\n\n\n"+
               "Run!\n\n"+
               "Un gioco scritto, disegnato e programmato da Pietro Martello.\n\n"+
               "Sviluppato con Processing 2, Paint, Microsoft Picture Manager.\n\n"+
               "Musica utilizzata:\n"+ 
               "'Dust Bowl', scritta e registrata da The Magnetic Fields, 1994.\n"+
               "'Oh, My Stars' scritta e registrata da Tanooki Suit, 2013.\n"+
               "'Once In A Lifetime' scritta e registrata da Luca Capitani.\n"+
               "'Epic' scritta e registrata da Calexico. 2012.\n"+
               "'Crank Heart', scritta e registrata da Xiu Xiu, 2004.\n"+
               "'A Warm Place' scritta e registrata da Nine Inch Nails, 1994.\n"+
               "'I Nearly Married A Human' scritta e registrata da The Tubeway Army, 1979.\n"+
               "Sono state utilizzate immagini tratte dai videoclip di 'Jerry Was A Racecar Driver' dei Primus e 'I Do What I Want, When I Want' degli Xiu Xiu.\n\n"+
               "Il Team (che sono io) desidera ringraziare:\n"+
               "Tutti quelli che hanno reso ciò possibile, tra cui:\n"+
               "Zappi\n"+
               "Pietro 'Da Motafakka'\n"+
               "Du Tano\n"+
               "Lallario e Fabiana\n"+
               "Musco"+
               "Altri tizi simpatici\n";
    String n = "Uuuuuuh sembra che qualcuno abbia attivato la God Mode per completare il gioco!\n"+
               "E allora io il vero testo finale non te lo faccio vedere.\n\n"+
               "Ecco.\n\n\n"+
               "Così impari come ci si sente quando qualcuno usa un trucchetto da due soldi buttando al vento tutto il tuo lavoro.\n"+
               "Ti è piaciuto il giretto? Completa il gioco in una delle altre difficoltà per avere il vero finale.\n\n\n"+
               "Però ti dirò che...\n"+
               "Sei stato colpito " + colpito + " volte.\n";
    if(colpito < 10)  n += "Bé, se il signorino è così bravo perché non ha abbastanza coraggio da abilitare il Game Over?";
    else if(colpito < 30)  n+= "Tieni duro ragazzo, un giorno ce la farai.";
    else              n += "Sai, accendere un gioco ed andarsene subito dopo lasciando il computer a giocare da solo è molto scortese.";
    textFont(font);
    textSize(17);
    fill(c);
    text("Menù Principale",startX,startY,ingombroX,20);
    textSize(20);
    fill(0);
    if(diff != 0)  text(s,200,credits,500,2200);  // A seconda della difficoltà solo una delle stringhe viene mostrata.
    else           text(n,200,credits,500,2200);
    creditime++;
    if(creditime == 4){
      credits -= 1;
      creditime = 0;
    }
  }
  
  void displayGameOver(){
    int startX = width/2 - 120, startY = 100, ingombroX = 240, ingombroY = 72;
    color c = color(0);
    textFont(gameo);
    textSize(72);
    fill(c);
    text("Game Over",startX,startY,ingombroX,ingombroY);
    
    // Pulsante per tornare al menù principale.
    if(mouseX > startX-20 && mouseX < startX+ingombroX/2 && mouseY > startY+ingombroY && mouseY < startY+ingombroY+17){
      c = color(0,0,255);
      if(mousePressed){
        pano.setNewGame();
        h.reset();
        resetEnemies();
        punteggio = 0;
        stato = 1;
      }
    }
    textFont(font);
    textSize(17);
    fill(c);
    text("Menù Principale",startX-20,startY+ingombroY,ingombroX,17);
    fill(255,0,0);
    text("Punteggio: "+punteggio/10,startX+ingombroX/2+30,startY+ingombroY,ingombroX,17);  // Mostra il tuo punteggio al momento della morte.
  }
  
  void resetEnemies(){  // Questo metodo viene chiamato ad ogni passaggio di livello. Resetta tutti i nemici in modo che possano venire riutilizzati in un livello successivo senza spuntare d'improvviso.
    for(int i=1; i<nemici.length; i++){
      nemici[i].reset();
    }
    if(pano.whereAreWeNow() == 1){  // Le spine sono l'unico nemico che non viene resettato quasi mai perché compaiono in 3 livelli su 4.
      nemici[nemici.length-1].coorX = -nemici[nemici.length-1].lun;  // La pozione deve essere resettata all'inizio del gioco perché nel primo livello non compare.
      nemici[0].reset();
    }
    if(pano.whereAreWeNow() == 3 && diff == 1){
      nemici[0].reset();
    }
  }
  
  void commandEnemies(){  // Questo metodo dice quali nemici muovere a seconda del livello.
    if(!pano.getCambio()){  // I nemici non compaiono mentre il livello cambia.
      if(pano.whereAreWeNow() == 1){
        nemici[1].disegna();
        nemici[2].disegna();
        if(diff == 2)  nemici[5].disegna();
      }
      else if(pano.whereAreWeNow() == 2){
        nemici[3].disegna();
        if(diff == 2) nemici[4].disegna();
        nemici[6].disegna();
        nemici[7].disegna();
      }
      else if(pano.whereAreWeNow() == 3){
        if(diff != 1) nemici[8].disegna();
        nemici[9].disegna();
        if(diff != 2) nemici[10].disegna();
        nemici[5].disegna();
      }
      else if(pano.whereAreWeNow() == 4){
        nemici[11].disegna();
        nemici[12].disegna();
        nemici[13].disegna();
      }
    }
  }
  
  
}
