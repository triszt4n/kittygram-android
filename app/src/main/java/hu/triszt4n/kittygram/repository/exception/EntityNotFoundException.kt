package hu.triszt4n.kittygram.repository.exception

import java.lang.RuntimeException

class EntityNotFoundException(message: String?) : RuntimeException(message)
