package com.noahedu.demo.mybatis.core;

import java.util.List;

public interface DaoGeneric<T> {

	/**
	 * Melakukan query jumlah record : <i> select count(*) .. </i>
	 * 
	 * @return Jumlah Record
	 */
	public int count();
	/**
	 * Melakukan query jumlah record yang sudah di status deleted
	 * 
	 * @return Jumlah Record
	 */
	public int countTrash();
	/**
	 * Melakukan query untuk ngambil Max ID + 1
	 * 
	 * @return ID Baru
	 */
	public int newId();
	/**
	 * 
	 * Insert record baru
	 * 
	 * @param object Entity sebuah tabel
	 */
	public void add(T object);
	/**
	 * 
	 * Set status record deleted, data masih ada tapi tidak terlihat di aplikasi
	 * 
	 * @param id ID record
	 */
	public void delete(int id);
	/**
	 *
	 * Set status record deleted, data masih ada tapi tidak terlihat di aplikasi
	 *
	 * @param id ID record
	 * @param permanent default is false, set true to remove from database permanently
	 */
	public void delete(int id, boolean permanent);
	/**
	 * 
	 * Mengembalikan status record deleted seperti sebelumnya agar terlihat di aplikasi
	 * 
	 * @param id ID record
	 */
	public void restore(int id);
	/**
	 * 
	 * Update data
	 * 
	 * @param object Entity data
	 */
	public void update(T object);
	/**
	 * Mengosongkan permanen isi tabel
	 */
	public void empty();
	/**
	 * Menghapus permanen record-record yang statusnya deleted
	 */
	public void clean();
	/**
	 * 
	 * Ngambil satu record tertentu berdasarkan ID
	 * 
	 * @param id ID record
	 * @return Object data
	 */
	public T get(int id);
	/**
	 * 
	 * Ngambil satu record tertentu berdasarkan ID lengkap dengan detail collection (One2Many)
	 * 
	 * @param id ID record
	 * @return Object data
	 */
	public T getWithDetailCollection(int id);
	
	/**
	 * 
	 * Query semua data
	 * 
	 * @return List data object
	 */
	public List<T> list();
	/**
	 * 
	 * Query list data
	 * 
	 * @param filter Where clause kolom data tertentu 
	 * @return List data object
	 */
	public List<T> list(List<Clause> filter);
	/**
	 * 
	 * Query list data
	 * 
	 * 
	 * @param filter Where clause kolom data tertentu
	 * @param order Order by kolom <i>order: kolom1 asc, kolom2 desc</i>
	 * @return List data object
	 */
	public List<T> list(List<Clause> filter, String order);
	/**
	 * 
	 * Query list data
	 * 
	 * @param index Index awal record
	 * @param count Jumlah record yang di retrieve
	 * @return List data object
	 */
	public List<T> list(int index, int count);
	/**
	 * 
	 * Query list data
	 * 
	 * @param index Index awal record
	 * @param count Jumlah record yang di retrieve
	 * @param order Order by kolom <i>order: kolom1 asc, kolom2 desc</i>
	 * @return List data object
	 */
	public List<T> list(int index, int count, String order);
	/**
	 * 
	 * Query list data
	 * 
	 * @param filter Where clause kolom data tertentu
	 * @param index Index awal record
	 * @param count Jumlah record yang di retrieve
	 * @return List data object
	 */
	public List<T> list(List<Clause> filter, int index, int count);
	/**
	 * 
	 * Query list data
	 * 
	 * @param filter Where clause kolom data tertentu
	 * @param index Index awal record
	 * @param count Jumlah record yang di retrieve
	 * @param order Order by kolom <i>order: kolom1 asc, kolom2 desc</i>
	 * @return List data object
	 */
	public List<T> list(List<Clause> filter, int index, int count, String order);
}
