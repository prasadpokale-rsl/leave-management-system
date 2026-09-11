package com.example.Laeave_Management_Api.Repository;

import com.example.Laeave_Management_Api.Model.EmployeeLeave;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class LeaveRepository {

    ConcurrentHashMap<Long, EmployeeLeave> hashMap = new ConcurrentHashMap<>();

    private final AtomicLong idGenerator = new AtomicLong();

    public List<EmployeeLeave> get(){
        return hashMap.values().stream().toList();
    }

    public EmployeeLeave getByID(Long id){
        return hashMap.get(id);
    }

    public EmployeeLeave save(EmployeeLeave emp) {

        if (emp.getId() == null) {
            emp.setId(idGenerator.incrementAndGet());
        }

        hashMap.put(emp.getId(), emp);

        return emp;
    }

    public void deleteByID(Long id){
        hashMap.remove(id);
    }
}
