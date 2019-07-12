#include <stdio.h>
#include <netdb.h>
#include <fcntl.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h> // gestion adresses i
#include <sys/types.h>
#define SERV "127.0.0.1" // adresse IP = boucle locale
#define PORT 12345 // port d’ecoute serveur

int port,sock; // n°port et socket
char mess[80]; // chaine de caracteres
char mess2[80];

struct sockaddr_in serv_addr; // zone adresse
struct hostent *server; // nom serveur

creer_socket()
{
  port = PORT;
  server = gethostbyname(SERV); // verification existance adresse

  if (!server){fprintf(stderr, "Problème serveur \"%s\"\n", SERV);exit(1);}

  // creation socket locale
  sock = socket(AF_INET, SOCK_STREAM, 0); // creation socket
  bzero(&serv_addr, sizeof(serv_addr)); // preparation champs entete
  serv_addr.sin_family = AF_INET; // Type d’adresses
  bcopy(server->h_addr, &serv_addr.sin_addr.s_addr,server->h_length);
  serv_addr.sin_port = htons(port); // port de connexion du serveur
}


main()
{
  // creation socket
  creer_socket();
 
  // connexion au serveur
  // connexion à l'application du dessus
  if (connect(sock, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0)
  {
    perror("Connexion impossible:");
    exit(1);
  }

  printf ("connexion avec serveur ok\n");
  // saisie d'une chaine de caractères au clavier
 

  printf ("Entrer un message : ");
  gets(mess);

  // envoi de cette chaine à l'application du dessus
  write(sock,mess,80);

  read(sock, mess2, 80);
  while(strcmp(mess2, "stop") != 0) {
    printf("le serveur me dit %s \n", mess2);
    gets(mess);
    write(sock, mess, 80);
    read(sock, mess2, 80);
  }

  close (sock);
} 