package io.github.dutianze.springbootthymeleafreact.shared.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
public class PageDto<T> {

  private List<T> content = new ArrayList<>();
  private int page;
  private int size;
  private long totalElements;
  private int totalPages;
  private boolean last;
  private int currentPage; // 1-based for URL
  private long pageStart;
  private long pageEnd;

  public PageDto(List<T> content, int page, int size, long totalElements, int totalPages,
      boolean last) {
    this.content = content;
    this.page = page;
    this.size = size;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
    this.last = last;
    this.currentPage = page + 1;
    this.pageStart = totalElements == 0 ? 0 : (long) page * size + 1;
    this.pageEnd = Math.min((long) (page + 1) * size, totalElements);
  }

  public static <T> PageDto<T> from(Page<T> page) {
    return new PageDto<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages(),
        page.isLast()
    );
  }
}
