# SmartCity
Stay tuned... A lot to come!

## To run the project: :boom:

- Open Android Studio
- File -> New -> Project from Version Control
- Type 'git@github.com:ir2010/SmartCity.git' in URL field and clone
- After the project is ready, run the app in an Android phone or an emulator to test


## To contribute to the project: 💻

- Create a new branch (give the branch a name that explains the work you're doing)
```
git branch <branch-name>
```
- Switch to the branch
```
git checkout <branch-name>
```
- Check existing branches
```
git branch
```

- Do the work. After completed, check the files that are changed
```
git status
```
- If there are files that you didn't wish to change but got changed by mistake, go to _.gitignore_ and add those files there.
- Now, add these files and commit
```
git add .
git commit -m "message explaining work done"
```
- Push the work on the remote repo in your respective branch
```
git push --set-upstream origin <branch-name>
```


### Keep updating your local project copy with the remote repo main branch
```
git pull origin main
```


### If the work on the particular branch is completed and you wish to merge with main branch :warning: 

```diff
- (Only after everything is working properly) -

git checkout main
git merge <branch-name>

RESOLVE THE CONFLICTS(if any), ADD, COMMIT, AND PUSH
```
## Flow of the app
<img src="https://github.com/ir2010/SmartCity/blob/main/app/src/main/res/drawable/smartcity.jpg" width="500px;" alt=""/>









