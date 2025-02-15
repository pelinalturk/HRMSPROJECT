package kodlamaio.Hrms.business.concretes;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import kodlamaio.Hrms.business.abstracts.JobAdvertisementService;
import kodlamaio.Hrms.core.utilities.dtoConverter.DtoConverterService;
import kodlamaio.Hrms.core.utilities.result.DataResult;
import kodlamaio.Hrms.core.utilities.result.Result;
import kodlamaio.Hrms.core.utilities.result.SuccessDataResult;
import kodlamaio.Hrms.core.utilities.result.SuccessResult;
import kodlamaio.Hrms.dataAccess.abstracts.JobAdvertisementDao;
import kodlamaio.Hrms.entities.concretes.JobAdvertisement;
import kodlamaio.Hrms.entities.dtos.JobAdvertisementAddDto;
import kodlamaio.Hrms.entities.dtos.JobAdvertisementDto;
import kodlamaio.Hrms.entities.dtos.JobAdvertisementsFilterDto;

@Service
public class JobAdvertisementManager implements JobAdvertisementService{

	private JobAdvertisementDao JobAdvertisementDao;
	private DtoConverterService dtoConverterService;
	
	@Autowired
	public JobAdvertisementManager(JobAdvertisementDao jobAdvertisementDao, DtoConverterService dtoConverterService) {
		super();
		this.JobAdvertisementDao = jobAdvertisementDao;
		this.dtoConverterService=dtoConverterService;
	}

	@Override
	public Result add(JobAdvertisementAddDto jobAdvertisementAddDto) {
		
		 this.JobAdvertisementDao.save((JobAdvertisement) dtoConverterService.dtoClassConverter(jobAdvertisementAddDto, JobAdvertisement.class) );
		 return new SuccessResult("Eklendi");
	}


	@Override
	public DataResult<List<JobAdvertisementDto>> findAllByIsActive(boolean active) {
		return new SuccessDataResult<List<JobAdvertisementDto>>
		(dtoConverterService.dtoConverter(JobAdvertisementDao.findByIsActive(active),JobAdvertisementDto.class));
	}


	@Override
	public DataResult<List<JobAdvertisementDto>> getByEmployer_CompanyName(String companyName) {
		return new SuccessDataResult<List<JobAdvertisementDto>>
		(this.dtoConverterService.dtoConverter(this.JobAdvertisementDao.getByEmployer_CompanyName(companyName),JobAdvertisementDto.class), "Şirket ismine göre listelendi.");
	}

	@Override
	public DataResult<List<JobAdvertisementDto>> findAllByIsActiveTrueOrderByCreatedDateAsc() {
		
		return new SuccessDataResult<List<JobAdvertisementDto>>
		(this.dtoConverterService.dtoConverter(this.JobAdvertisementDao.findByIsActiveTrueOrderByCreatedDateAsc(),JobAdvertisementDto.class), "Aktif iş ilanları oluşturulma tarihine göre listelendi.");
	}

	@Override
	public Result update(JobAdvertisement jobAdvertisement) {
		this.JobAdvertisementDao.save(jobAdvertisement);
		return new SuccessResult("güncellendi");
	}

	@Override
	public DataResult<List<JobAdvertisementDto>> getAll() {
		return new SuccessDataResult<List<JobAdvertisementDto>>
		(this.dtoConverterService.dtoConverter(this.JobAdvertisementDao.findAll(),JobAdvertisementDto.class),"Data Listelendi");
	}

	@Override
	public JobAdvertisement getById(int id) {
		return this.JobAdvertisementDao.findById(id).get();
	}

	@Override
	public DataResult<List<JobAdvertisementDto>> findByIsConfirm( boolean confirm) {
		return new SuccessDataResult<List<JobAdvertisementDto>>
		(dtoConverterService.dtoConverter(JobAdvertisementDao.findByIsConfirm(confirm),JobAdvertisementDto.class));
	}

	@Override
	public List<JobAdvertisement> getByEmployerId(int id) {
		return this.JobAdvertisementDao.getByEmployer_Id(id);
	}

	@Override
	public DataResult<List<JobAdvertisement>> findByIsActiveTrueAndIsConfirmTrue() {
		return new SuccessDataResult<List<JobAdvertisement>>
		(this.JobAdvertisementDao.findByIsActiveTrueAndIsConfirmTrue(), "Aktif Ve Onaylı İş İlanları Listelendi.");
	}

	@Override
	public DataResult<List<JobAdvertisement>> findByIsActiveTrueAndIsConfirmTrue(int pageNo, int pageSize) {
		PageRequest pageable = PageRequest.of(pageNo-1, pageSize);
		return new SuccessDataResult<List<JobAdvertisement>>(this.JobAdvertisementDao.findAll(pageable).getContent());
	}

	@Override
	public DataResult<List<JobAdvertisement>> getByPositionLevelId(int id) {
		return new SuccessDataResult<List<JobAdvertisement>>
		(this.JobAdvertisementDao.getByPositionLevelId(id), "Data Listelendi");
	}

	@Override
	public DataResult<List<JobAdvertisement>> getByJobPositionId(int id) {
		return new SuccessDataResult<List<JobAdvertisement>>
		(this.JobAdvertisementDao.getByJobPositionId(id), "Data Listelendi");
	}

	@Override
	public DataResult<List<JobAdvertisement>> getByFilterJob(int pageNo, int pageSize,JobAdvertisementsFilterDto jobAdvertisementFilter) {
		 Pageable pageable = PageRequest.of(pageNo -1, pageSize);
	     return new SuccessDataResult<List<JobAdvertisement>>
	    (this.JobAdvertisementDao.getByFilter(jobAdvertisementFilter, pageable).getContent(), 
	     this.JobAdvertisementDao.getByFilter(jobAdvertisementFilter,pageable).getTotalElements()+"");
	}

	@Override
	public Result isActiveChange(int id) {
		JobAdvertisement jobAdvertisement = JobAdvertisementDao.getById(id);
		jobAdvertisement.setActive(false);
		JobAdvertisementDao.save(jobAdvertisement);
		return new SuccessResult("İş ilanı yayından kaldırıldı.");
	}

	@Override
	public List<JobAdvertisement> activeTrue(int pageNo, int pageSize) {
		PageRequest pageable = PageRequest.of(pageNo-1, pageSize);
		return this.JobAdvertisementDao.activeTrue(pageable);
	}
}