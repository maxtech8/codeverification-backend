package org.backend.service;

import org.json.JSONObject;
import org.json.JSONException;

public interface ParentService {
    // Retrieves a list of parents
    JSONObject getParents(int page_num) throws JSONException;
}
