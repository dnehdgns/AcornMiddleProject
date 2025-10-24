package event;

import java.util.List;

public class CategoryService {

	CategoryDAO dao = new CategoryDAO();
	
	//카테고리 이름 조회
	public List<Category> getCategoryList(){
		return dao.findAllCategory();
	}
	
}
