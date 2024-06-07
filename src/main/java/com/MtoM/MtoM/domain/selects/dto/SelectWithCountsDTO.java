package com.MtoM.MtoM.domain.selects.dto;

import com.MtoM.MtoM.domain.selects.domain.SelectDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SelectWithCountsDTO {
    private SelectDomain select;
    private int viewCount;
    private int heartCount;
}