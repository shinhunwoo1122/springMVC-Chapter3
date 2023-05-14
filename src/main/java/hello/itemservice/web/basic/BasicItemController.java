package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }
   // @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model){

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);
        model.addAttribute("item",item);
        return "basic/item";
    }

    /**
     * @ModelAttribute "" naming 을 줄시 model에 해당이름으로 주입해주는 기능이 있음
     */
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item,
                       Model model){

        itemRepository.save(item);
        //model.addAttribute("item",item);
        return "basic/item";
    }

    /**
     * @ModelAttribute "" naming 을 안줄시 Item 형식일시 -> item으로 model에 넣어 줌
     */
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item,
                            Model model){

        itemRepository.save(item);
        //model.addAttribute("item",item);
        return "basic/item";
    }

    /**
     * @ModelAttribute도 제외 가능함
     */
   // @PostMapping("/add")
    public String addItemV4(Item item){

        itemRepository.save(item);
        //model.addAttribute("item",item);
        return "basic/item";
    }

    /**
     * 상품 등록 후 페이지로 return할시 세로고침하거나 했을시
     * 같은 데이터로 여러번 들어오는 이슈 발생
     * 이를 방지하기 위해 POST/Redirect/GET으로 처리해줌
     */
    //@PostMapping("/add")
    public String addItemV5(Item item){

        itemRepository.save(item);
        return "redirect:/basic/items/"+ item.getId();
    }

    /**
     * "redirect:/basic/items/"+ item.getId(); 이런식으로 URL 연산으로 처리 할시
     * 데이터 인코딩과 띄어쓰기가 들어가거나 하면 위험하기 떄문에
     * RedirectAttributes를 사용하여 처리함 인코딩 처리 해결 가능 해짐
     */
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes){

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item){
        itemRepository.update(itemId,item);
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
