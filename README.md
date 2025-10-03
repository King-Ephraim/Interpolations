# 📈 Interpolations Polynomiales en JavaFX

Ce projet illustre et compare différentes méthodes d’**interpolation polynomiale** :  
- **Lagrange**
- **Newton (différences divisées)**
- **Moindres carrés**

Il affiche les résultats sous forme de **graphiques interactifs** grâce à **JavaFX**.

---

## 🚀 Fonctionnalités
- Visualisation graphique des points d’origine
- Courbe interpolée par la méthode de Lagrange
- Courbe interpolée par la méthode de Newton
- Ajustement polynomial par la méthode des Moindres carrés
- Interface claire via un `LineChart` JavaFX

---

## 📂 Structure
- `HelloApplication.java` : application JavaFX principale
- `Gauss.java` : solveur du système linéaire pour la méthode des moindres carrés
- `resources/` : fichiers FXML et ressources JavaFX (si nécessaire)

---

## ⚙️ Exécution
### Prérequis
- Java **17+**
- JavaFX installé (ou via Maven/Gradle)

### Lancer le projet
```bash
mvn clean javafx:run
