package com.nghiangong.address;

import com.nghiangong.address.repository.DistrictRepository;
import com.nghiangong.address.repository.ProvinceRepository;
import com.nghiangong.address.repository.WardRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressService {
    ProvinceRepository provinceRepository;
    DistrictRepository districtRepository;
    WardRepository wardRepository;
    AddressMapper addressMapper;

    public List<String> getProvinceNames() {
        return provinceRepository.findAllProvinceNames();
    }

    public List<String> getDistrictNames(String provinceFullName) {
        return districtRepository.findDistrictNamesByProvinceFullName(provinceFullName);
    }

    public List<String> getWardNames(String districtFullName) {
        return wardRepository.findWardNamesByDistrictFullName(districtFullName);
    }

    public void initData(DataRequest dataRequest) {
        for (var provinceData : dataRequest.getData()) {
            var province = provinceRepository.save(addressMapper.toProvince(provinceData));
            for (var districtData : provinceData.getData2()) {
                var district = addressMapper.toDistrict(districtData);
                district.setProvince(province);
                district = districtRepository.save(district);
                for (var wardData : districtData.getData3()) {
                    var ward = addressMapper.toWard(wardData);
                    ward.setDistrict(district);
                    wardRepository.save(ward);
                }
            }
        }
    }
}
