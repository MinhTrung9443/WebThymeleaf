package vn.iotstar.controller.admin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.*;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import vn.iotstar.entity.*;
import vn.iotstar.model.*;
import vn.iotstar.service.categoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/category")
public class adminController {
	@Autowired
	categoryService cateservice;

	@RequestMapping("")
	public String all(Model model) {
		List<Category> list = cateservice.findAll();
		model.addAttribute("list", list);
		return "views/admin/category/list";
	}

	@GetMapping("/add")
	public String add(Model model) {
		categoryModel cateModel = new categoryModel();
		cateModel.setEdit(false);
		model.addAttribute("category", cateModel);
		return "views/admin/category/add";
	}

	@PostMapping("/save")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("category") categoryModel cateModel,
			BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("forward:/admin/category", model);
		}
		Category entity = new Category();
		BeanUtils.copyProperties(cateModel, entity);
		if (entity.getImage() == "" || entity.getImage() == null) {
			Optional<Category> opt = cateservice.findById(entity.getId());
			Category temp = opt.get();
			entity.setImage(temp.getImage());
		}
		cateservice.save(entity);
		String message = "";
		if (cateModel.isEdit() == true) {
			message = "edited";
		} else {
			message = "saved";
		}
		model.addAttribute("message", message);

		return new ModelAndView("forward:/admin/category", model);
	}

	@GetMapping("/edit/{id}")
	public ModelAndView edit(ModelMap model, @PathVariable("id") Long categoryId) {
		Optional<Category> optCategory = cateservice.findById(categoryId);
		categoryModel cateModel = new categoryModel();
		if (optCategory.isPresent()) {
			Category entity = optCategory.get();

			BeanUtils.copyProperties(entity, cateModel);
			cateModel.setEdit(true);

			model.addAttribute("category", cateModel);

			return new ModelAndView("views/admin/category/add", model);
		}
		model.addAttribute("message", "Category is not existed!!");
		return new ModelAndView("forward:/admin/category", model);

	}

	@GetMapping("/delete/{id}")
	public ModelAndView delete(ModelMap model, @PathVariable("id") Long categoryId) {
		Optional<Category> optCategory = cateservice.findById(categoryId);
		if (optCategory.isPresent()) {
			cateservice.deleteById(categoryId);
			List<Category> list = cateservice.findAll();
			model.addAttribute("list", list);
			return new ModelAndView("views/admin/category/list", model);
		}
		model.addAttribute("message", "Category is not existed!!");
		return new ModelAndView("forward:/admin/category", model);

	}

	@GetMapping("/search")
	public String search(Model model) {
		String name = "";
		model.addAttribute("name", name);
		return "views/admin/category/search";
	}

	@RequestMapping("/searchlist")
	public String search(ModelMap model, @RequestParam(name = "name", required = false) String name,
			@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
		int count = (int) cateservice.count();
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(3);
		Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name"));
		Page<Category> resultPage = null;
		if (StringUtils.hasText(name)) {
			resultPage = cateservice.findByNameContaining(name, pageable);
			model.addAttribute("name", name);
		} else {
			resultPage = cateservice.findAll(pageable);
		}
		int totalPages = resultPage.getTotalPages();
		if (totalPages > 0) {
			int start = Math.max(1, currentPage - 2);
			int end = Math.min(currentPage + 2, totalPages);
			if (totalPages > count) {
				if (end == totalPages)
					start = end - count;
				else if (start == 1)
					end = start + count;
			}
			List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
			model.addAttribute("categoryPage", resultPage);
		}
		return "views/admin/category/searchlist";
	}

}
