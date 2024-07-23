package com.eazybytes.cards.service.impl;

import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.eazybytes.cards.constants.CardsConstants;
import com.eazybytes.cards.dto.CardDto;
import com.eazybytes.cards.entity.Card;
import com.eazybytes.cards.exception.CardAlreadyExistsException;
import com.eazybytes.cards.exception.ResourceNotFoundException;
import com.eazybytes.cards.mapper.CardMapper;
import com.eazybytes.cards.repository.CardRepository;
import com.eazybytes.cards.service.ICardService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements ICardService {
	private final CardRepository cardRepository;

	@Override
	public void createCard(String mobileNumber) {
		Optional<Card> optionalCard = cardRepository.findByMobileNumber(mobileNumber);
		optionalCard.ifPresent(c -> {
			throw new CardAlreadyExistsException("Card already exists with given mobile number: " + c.getMobileNumber());
		});

		cardRepository.save(createNewCard(mobileNumber));
	}

	@Override
	public CardDto fetchCard(String mobileNumber) {
		Card card = cardRepository.findByMobileNumber(mobileNumber)
			.orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));
		return CardMapper.mapToCardDto(card, new CardDto());
	}

	@Override
	public boolean updateCard(CardDto cardDto) {
		Card card = cardRepository.findByCardNumber(cardDto.getCardNumber())
			.orElseThrow(() -> new ResourceNotFoundException("Card", "cardNumber", cardDto.getCardNumber()));
		CardMapper.mapToCard(cardDto, card);
		cardRepository.save(card);
		return true;
	}

	@Override
	@Transactional
	public boolean deleteCard(String mobileNumber) {
		Card card = cardRepository.findByMobileNumber(mobileNumber)
			.orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));
		cardRepository.deleteById(card.getCardId());
		return true;
	}

	private Card createNewCard(String mobileNumber) {
		Card newCard = new Card();
		long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
		newCard.setCardNumber(Long.toString(randomCardNumber));
		newCard.setMobileNumber(mobileNumber);
		newCard.setCardType(CardsConstants.CREDIT_CARD);
		newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
		newCard.setAmountUsed(0);
		newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
		return newCard;
	}

}
