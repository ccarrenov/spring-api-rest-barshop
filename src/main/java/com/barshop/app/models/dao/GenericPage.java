package com.barshop.app.models.dao;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GenericPage <E> implements Page <E>{

	private List<E> elements;
	private int number;
	private int size;
	private int totalPage;
	private Sort sort;
	private long totalElements;
	

	@Override
	public int getNumber() {
		return number;
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public int getNumberOfElements() {
		return elements.size();
	}

	@Override
	public List<E> getContent() {
		return elements;
	}

	@Override
	public boolean hasContent() {
		return !elements.isEmpty();
	}

	@Override
	public Sort getSort() {
		
		return sort;
	}

	@Override
	public boolean isFirst() {
		return number == 1;
	}

	@Override
	public boolean isLast() {
        return number == totalPage;
	}

	@Override
	public boolean hasNext() {
		return (number < totalPage);
	}

	@Override
	public boolean hasPrevious() {
		return (0 < number);
	}

	@Override
	public Pageable nextPageable() {
		return null;
	}

	@Override
	public Pageable previousPageable() {
		return null;
	}

	@Override
	public Iterator<E> iterator() {
		return elements.iterator();
	}

	@Override
	public int getTotalPages() {
		return totalPage;
	}

	@Override
	public long getTotalElements() {		
		return totalElements;
	}

	@Override
	public <U> Page<U> map(Function<? super E, ? extends U> converter) {
		return null;
	}
	
}
