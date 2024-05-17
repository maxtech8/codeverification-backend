package org.backend.service;

import org.backend.model.Parent;
import org.backend.repository.ParentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;
import org.backend.repository.ChildRepository;

@Service
public class ParentServiceImpl implements ParentService {
    private final ParentRepository parentRepository;
	private final ChildRepository childRepository;
	
    // Constructor injection of ParentRepository
    public ParentServiceImpl(ParentRepository parentRepository, ChildRepository childRepository) {
        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
    }
	
    @Override
    public JSONObject getParents(int page_num) throws JSONException {
    	
    	Pageable pageable = PageRequest.of(page_num, 2, Sort.by("id").ascending());
    	
    	int total_num = parentRepository.findAll(pageable).getTotalPages();
    	
        // Retrieve a page of parents from the repository, ordered by creation date in descending order
        List<Parent> pageResult =  parentRepository.findAll(pageable).getContent();
        
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        
        for(Parent parent: pageResult) {
        	JSONObject tempObject = parent.toJsonParent();
        	tempObject.put("total_paid_amount", childRepository.sumPaidAmount(parent.getId()));
        	
        	jsonArray.put(tempObject);
        }
        
        
        jsonObject.put("total_num", total_num);
        jsonObject.put("parents", jsonArray);
        
        return jsonObject;
    }
}
