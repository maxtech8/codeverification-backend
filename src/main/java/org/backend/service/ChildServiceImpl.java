package org.backend.service;

import org.backend.model.Child;
import org.backend.model.Parent;
import org.backend.repository.ChildRepository;
import org.backend.repository.ParentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

@Service
public class ChildServiceImpl implements ChildService {
    private final ChildRepository childRepository; // Repository for child entities
    private final ParentRepository parentRepository; // Repository for parent entities

    // Constructor with required arguments injection
    public ChildServiceImpl(ChildRepository childRepository, ParentRepository parentRepository) {
        this.childRepository = childRepository;
        this.parentRepository = parentRepository;
    }

    @Override
    public List<Child> getChildren() {
        return childRepository.findAllByOrderByCreatedAtDesc(); // Retrieve all children entities in descending order of creation
    }

    @Override
    public JSONObject getChildrenByParentId(Long parent_id, int page_num) throws JSONException {
    	try {
    		Pageable pageable = PageRequest.of(page_num, 2, Sort.by("id").ascending());
        	
        	int total_page = childRepository.findChildrenByParentId(parent_id, pageable).getTotalPages();
        	
        	JSONArray childrenArray = new JSONArray();
            List<Child> children = childRepository.findChildrenByParentId(parent_id, pageable).getContent(); // Retrieve children entities by parent ID
            
            Parent parent = parentRepository.findById(parent_id); // Retrieve the parent entity by ID
            
            for (Child child : children) {
                child.setParent(parent); // Set the parent for each child entity
                
                childrenArray.put(child.toJsonChild());
            }
           
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("children", childrenArray);
            jsonObject.put("total_page", total_page);
            
            return jsonObject; // Return the list of children entities with their respective parent
    	} catch(JSONException e) {
            e.printStackTrace(); // Print the stack trace for debugging purposes
            throw new JSONException("An error occurred while retrieving children by parent ID: " + e.getMessage());
    	}
    	
    }
    
}
