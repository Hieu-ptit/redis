package com.viettel.core.service.impl;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.JsonWriter;
import com.dslplatform.json.runtime.Generics;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.commons.exception.BusinessException;
import com.viettel.commons.util.ErrorCode;
import com.viettel.commons.util.Json;
import com.viettel.core.mapper.AccountMapperEntity2;
import com.viettel.core.mapper.CompanyMapperEntity2;
import com.viettel.core.mapper.Entity2EmployeeResponseMapper;
import com.viettel.core.mapper.RewardBoxMapperEntity;
import com.viettel.core.model.BoxInvitationMetadata;
import com.viettel.core.model.RewardBox;
import com.viettel.core.model.entity.Employee;
import com.viettel.core.model.request.EmployeeRequest;
import com.viettel.core.model.response.EmployeeResponse;
import com.viettel.core.repository.EmployeeRepository;
import com.viettel.core.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    private static final JsonWriter.WriteObject<Employee> metadataWriter = Json.findWriter(Employee.class);

    private static final JsonReader.ReadObject<Employee> metadataReader = Json.findReader(Employee.class);

    @Value("${app.session.changer.password.first.times}")
    private Long expireTimeChangePasswordTimes;

    private final JedisPool jedisPool;

    private static final JsonWriter.WriteObject<List<RewardBox>> dataWriter = Json.findWriter(Generics.makeParameterizedType(List.class, RewardBox.class));

    final ObjectMapper mapper = new ObjectMapper();

    /**
     * Save the user's username if this user logs in for the first time
     *
     * @param id
     * @return
     */
    public boolean verifySessionCode(String id) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(id);
        }
    }

    public String findToken(String id) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(id);
        }
    }
    public boolean saveSessionLoginFirstTime(String id, Long expireTime, Employee account) {
        try (Jedis jedis = jedisPool.getResource()) {
            expireTime = expireTime == null ? expireTimeChangePasswordTimes : expireTime;
            String saveSuccess = jedis.set(id, Json.encodeToString(account, metadataWriter), SetParams.setParams().ex(expireTime));
            return !StringUtils.isEmpty(saveSuccess);
        }
    }

    @Override
    @Transactional
    public long nextAccountId() {
        return repository.nextEmployeeId();
    }

    @Override
    public void create(EmployeeRequest request) throws JsonProcessingException {
        var boxInvitationMetadata = new BoxInvitationMetadata().setBoxId(request.getBoxId()).setName(request.getNameBox());
        var account = (AccountMapperEntity2.INSTANCE.map(request).setMetadata(boxInvitationMetadata)).setId(nextAccountId())
                .setCreatedAt(OffsetDateTime.now()).setUpdatedAt(OffsetDateTime.now());
        List<RewardBox> boxes = RewardBoxMapperEntity.INSTANCE.mapList(request.getBoxes());
        var company = CompanyMapperEntity2.INSTANCE.map(request.getCompany());
        account.setBoxes(boxes);
        account.setMetadata(boxInvitationMetadata);
        account.setCompany(company);
        saveSessionLoginFirstTime(account.getId().toString(), expireTimeChangePasswordTimes, account);
//        repository.insert(account);
        repository.create(account, mapper.writeValueAsString(account.getMetadata()), Json.encodeToString(boxes, dataWriter),
                mapper.writeValueAsString(company));
    }

    @Override
    public EmployeeResponse getEmployee(Long id) {
        var employee = repository.findById(id).orElseThrow(
                () -> new BusinessException(ErrorCode.EMPLOYEE_NOT_FOUND, ErrorCode.EMPLOYEE_NOT_FOUND.getMessage()));
        return Entity2EmployeeResponseMapper.INSTANCE.map(employee);
    }
}
