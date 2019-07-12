#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <sys/types.h>


#define PORT 12345
int sock, socket2, lg;
char mess[80];
char mess2[80];
struct sockaddr_in local; // création d'une structure pour les params réseaux locaux
struct sockaddr_in distant; // création d'une structure pour les params réseaux distant

creer_socket() {
    // preparation des champs d’entete
    bzero(&local, sizeof(local)); // remplis les params réseaux locaux avec des 0
    local.sin_family = AF_INET; // on utilise le protocole IPv4
    local.sin_port = htons(PORT); // on définit le port
    local.sin_addr.s_addr = INADDR_ANY; // cela concerne toutes les interfaces locales
    bzero(&(local.sin_zero),8); // ???

    lg = sizeof(struct sockaddr_in); // ???
    if((sock=socket(AF_INET, SOCK_STREAM,0)) == -1) {
        perror("socket");
        exit(1);
    }
}


main() {
    // on crée le socket
    creer_socket();

    // si on arrive pas à bind (port déjà utilisé on lance une erreur)
    if(bind(sock, (struct sockaddr *)&local, sizeof(struct sockaddr)) == -1) {
        perror("bind");
        exit(1);
    }

    // si on arrive pas à écouter alors on lance une erreur
    if(listen(sock, 5) == -1) {
        perror("listen");
        exit(1);
    }

    // en boucle
    while(1) {

        // bug de connexion
        if((socket2=accept(sock, (struct sockaddr *)&distant, &lg)) == -1) {
            perror("accept");
            exit(1);
        }

        // un client est connecté
        printf ("client connecte \n");
        read(socket2,mess,80);

        // on affiche le msg du client
        while(strcmp(mess, "stop") != 0) {
            printf ("le client me dit %s \n",mess);

            gets(mess2);
            write(socket2,mess2,80);

            read(socket2,mess,80);
        }
        
        close(socket2); // on ferme
    }

}
