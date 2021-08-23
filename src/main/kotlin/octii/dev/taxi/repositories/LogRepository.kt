package octii.dev.taxi.repositories

import octii.dev.taxi.models.database.LogModel
import org.springframework.data.jpa.repository.JpaRepository

interface LogRepository : JpaRepository<LogModel, Long>