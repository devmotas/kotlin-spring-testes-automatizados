package br.com.alura.forum.services

import br.com.alura.forum.exception.NotFoundException
import br.com.alura.forum.mapper.TopicoFormMapper
import br.com.alura.forum.mapper.TopicoViewMapper
import br.com.alura.forum.models.TopicoTest
import br.com.alura.forum.models.TopicoViewTest
import br.com.alura.forum.repository.TopicoRepository
import br.com.alura.forum.service.TopicoService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.*


class TopicoServiceTest {
    val topico = PageImpl(listOf(TopicoTest.build()))
    val paginacao : Pageable = mockk()
    val topicoRepository: TopicoRepository = mockk {
        every { findByCursoNome(any(), any()) } returns topico
        every { findAll(paginacao) } returns topico
    }

    val topicoViewMapper: TopicoViewMapper = mockk{
        every { map(any()) } returns TopicoViewTest.build()
    }

    val topicoFormMapper: TopicoFormMapper = mockk()

    val topicoService = TopicoService(
        topicoRepository,
        topicoViewMapper,
        topicoFormMapper
    )

    @Test
    fun `deve listar topicos a partir do nome do curso`(){
        topicoService.listar("Kotlin avançado", paginacao)

        verify(exactly = 1) {topicoRepository.findByCursoNome(any(), any())}
        verify(exactly = 1) {topicoViewMapper.map(any())}
        verify(exactly = 0) {topicoRepository.findAll(paginacao)}
    }

    @Test
    fun `deve listar todos os topicos quando o nome do curso for nulo`(){
        topicoService.listar(null, paginacao)

        verify(exactly = 0) {topicoRepository.findByCursoNome(any(), any())}
        verify(exactly = 1) {topicoRepository.findAll(paginacao)}
        verify(exactly = 1) {topicoViewMapper.map(any())}
    }

    @Test
    fun `deve listar not found exception quando topico não for encontrado`(){
        every { topicoRepository.findById(any()) } returns Optional.empty()

        val atual = assertThrows<NotFoundException> {
            topicoService.buscarPorId(1)
        }

        assertThat(atual.message).isEqualTo("Topico nao encontrado!")
    }
}