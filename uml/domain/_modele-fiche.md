# Modèle de fiche (à copier dans un groupe, titre à remplacer par « ## CUxx - Nom »)

## <ID> - <Nom du cas d'utilisation>

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | |
| Préconditions | |
| Scénario nominal | 1. ... |
| Scénarios alternatifs | A1 : ... |
| Exceptions | E1 : ... |
| Postconditions | |
| Règles | RG-xx |

```plantuml
actor Utilisateur as A
participant "App" as APP
participant "API" as API
A -> APP : action
APP -> API : nomOperation(parametres)
```
