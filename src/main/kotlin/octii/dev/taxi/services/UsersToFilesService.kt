package octii.dev.taxi.services

import octii.dev.taxi.models.database.UsersToFiles
import octii.dev.taxi.repositories.UsersToFilesRepository
import org.springframework.stereotype.Service

@Service
class UsersToFilesService(val usersToFilesRepository: UsersToFilesRepository) {

    fun saveFile(usersToFiles: UsersToFiles) : UsersToFiles {
        if (usersToFiles.user != null){
            val foundFile = usersToFilesRepository.findByUserIdAndIsNewAndType(usersToFiles.user!!.id, true, usersToFiles.type)
            if (foundFile != null){
                foundFile.isNew = false
                usersToFilesRepository.save(foundFile)
            }
        }
        return usersToFilesRepository.save(usersToFiles)
    }

    fun getAllNew(userId : Long) : List<UsersToFiles> {
        return usersToFilesRepository.findAllByUserIdAndIsNew(userId, true) ?: listOf()
    }

    fun delete(usersToFiles: UsersToFiles) = usersToFilesRepository.delete(usersToFiles)

    fun getByFileName(fileName : String) : UsersToFiles? = usersToFilesRepository.findByFileName(fileName)

}