package br.com.alura.forum.models

import br.com.alura.forum.model.Curso

object CursoTest {
    fun build() = Curso(id = 1, nome = "Kotlin Basico", categoria = "Programacao")
}