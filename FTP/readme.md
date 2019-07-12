# Serveur & client FTP

Implémentations simplistes d'un serveur et client FTP en Java dans le cadre du cours "Sécurité & Réseaux" en licence professionnelle APIDAE à l'IUT de Montpellier.

# Installation

 1. Compiler les fichiers sources : `javac *.java`
 2. Lancer le serveur : `java FtpServer`
 3. Lancer le client : `java FtpClient`
 4. Connectez vous au serveur depuis le client :
 **Adresse :** *adresse IP du serveur*
 **Port :** *12345*
**Utilisateur :** *ffctlr*
**Mot de passe :** *rsx_2018*
 5. Regardez le paragraphe 'Utilisation' pour voir les commandes disponibles.

# Utilisation

## RETR

    RETR monfichier.pdf
Télécharge le fichier monfichier.pdf depuis le serveur.
Vous devez spécifier un chemin relatif par rapport à l'exécutable du serveur ou utiliser un chemin absolu. 
Le fichier est téléchargé dans le dossier de l'exécutable du client.

## STOR

    STOR monfichier.pdf
Envoie le fichier monfichier.pdf vers le serveur.
Vous devez spécifier un chemin relatif par rapport à l'exécutable du client ou utiliser un chemin absolu. 
Le fichier est envoyé dans le dossier de l'exécutable du serveur.

## QUIT

    QUIT
Ferme la connexion au serveur.

# Crédits
SUREAU Benjamin
NICOT Bryan
