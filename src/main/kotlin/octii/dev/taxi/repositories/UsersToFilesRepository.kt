package octii.dev.taxi.repositories

import octii.dev.taxi.models.UsersToFiles
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsersToFilesRepository : JpaRepository<UsersToFiles, Long> {
    fun findByFileName(name : String) : UsersToFiles

    fun findByUserIdAndIsNewAndType(id : Long, isNew : Boolean, type : String) : UsersToFiles?
}