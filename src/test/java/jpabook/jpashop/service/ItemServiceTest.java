package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired ItemService itemService;
    @Autowired ItemRepository itemRepository;
    @Autowired EntityManager em;

    @Test
    void 상품_저장() {
        Album item = new Album();

        item.setName("promi1");
        item.setPrice(9876);
        item.setStockQuantity(50);
        item.setArtist("promise nine");
        item.setEtc("특이사항 없음");

        itemService.saveItem(item);

        Item getItem = itemService.itemOne(item.getId());

        em.flush();

        assertThat(item.getId()).isEqualTo(getItem.getId());
    }
}