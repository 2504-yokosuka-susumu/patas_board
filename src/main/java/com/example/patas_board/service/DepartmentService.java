package com.example.patas_board.service;

import com.example.patas_board.repository.DepartmentRepository;
import com.example.patas_board.repository.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    DepartmentRepository departmentRepository;
    /*
     * Department全件取得処理
     */
    public HashMap<Integer,String> findAllDepartmentsMap() {
        List<Department> results = departmentRepository.findAll();
        HashMap<Integer,String> departmentChoices = new HashMap<>();
        //キー値をid、値をnameのハッシュマップを作製
        for(int i=0;i<results.size();i++){
            Department result = results.get(i);
            departmentChoices.put(result.getId(), result.getName());
        }
        return departmentChoices;
    }
}
